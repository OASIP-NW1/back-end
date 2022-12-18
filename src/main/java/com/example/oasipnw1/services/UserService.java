package com.example.oasipnw1.services;


import com.example.oasipnw1.HandleValidationErrors;
import com.example.oasipnw1.Role;
import com.example.oasipnw1.config.JwtTokenUtil;
import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserDTO;
import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.dtos.UserUpdateDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.model.JwtResponse;
import com.example.oasipnw1.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
//import java.sql.Date;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    public List<UserDTO> getAllUser() {
        List<User> userList = userRepository.findAll((Sort.by("name").ascending()));
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(user, UserDTO.class);
    }

    public User save(UserCreateDTO newUser) {
        User addUserList = modelMapper.map(newUser, User.class);
        addUserList.setName(newUser.getName().trim());
        addUserList.setEmail(newUser.getEmail().trim());

        //  Check Validtion Unique Create (name email role)
        if (checkUniqueCreate(newUser)) {
            addUserList.setRole(newUser.getRole() == null ? Role.Student : newUser.getRole());
        }
        // Argon2PasswordEncoder : Add Password
        addUserList.setPassword(argon2PasswordEncoder.encode(addUserList.getPassword()));
        return userRepository.saveAndFlush(addUserList);
    }

    public UserUpdateDTO updateUser(UserUpdateDTO updateUser, Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
//        user.setName(updateUser.getName().trim());
//        user.setEmail(updateUser.getEmail().trim());

//       Check Update Role
        if (updateUser.getRole() == null) {
            updateUser.setRole(user.getRole());
        } else if (!(user.getRole().equals(updateUser.getRole()))) {
            user.setRole(updateUser.getRole());
        }
//        userRepository.saveAndFlush(user);
//        return updateUser;

//        Check Validtion Unique Update (name email role)
        if (checkUniqueUpdate(updateUser, id)) {
            user.setName(updateUser.getName().trim());
            user.setEmail(updateUser.getEmail().trim());
            user.setRole((updateUser.getRole() == null) ? user.getRole() : updateUser.getRole());
            userRepository.saveAndFlush(user);
        }
        return updateUser;
    }

    public boolean checkUniqueUpdate(UserUpdateDTO user, Integer id) {
        List<User> allUser = userRepository.findAll();
        for (User users : allUser) {
            if (!(users.getId() == id)) {
                if (users.getName().trim().equals(user.getName().trim())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name is already used");
                } else if (users.getEmail().trim().equals(user.getEmail().trim())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already used");
                }
            }
        }
        return true;
    }

    public boolean checkUniqueCreate(UserCreateDTO user) {
        List<User> allUser = userRepository.findAll();
        for (User users : allUser) {
            if (users.getName().trim().equals(user.getName().trim())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This name is already used");
            } else if (users.getEmail().trim().equals(user.getEmail().trim())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already used");
            }
        }
        return true;
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    public ResponseEntity loginDTO(@Valid UserLoginDTO userLogin,
                                   HttpServletResponse httpServletResponse,
                                   ServletWebRequest request) throws Exception {
        Map<String,String> errorMap = new HashMap<>();
        String status;

        if (userRepository.existsByEmail(userLogin.getEmail())) {
            System.out.println("0");
            User user = userRepository.findByEmail(userLogin.getEmail());
            if (argon2PasswordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
                System.out.println("1");
                authenticate(userLogin.getEmail(), userLogin.getPassword());
                authenticate(userLogin.getEmail(), userLogin.getPassword());

                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(userLogin.getEmail());

                final String token = jwtTokenUtil.generateToken(userDetails);

                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
                return ResponseEntity.ok(new JwtResponse("Login Success",token,refreshToken));
//                throw new ResponseStatusException(HttpStatus.OK, "Password Matched");
            } else {
                errorMap.put("message","Password NOT Matched");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                status = HttpStatus.UNAUTHORIZED.toString();
//            }
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        }

        HandleValidationErrors errors = new HandleValidationErrors(
                Date.from(Instant.now()),
                httpServletResponse.getStatus(),
                status,
                errorMap.get("message"),
                request.getRequest().getRequestURI());
        return ResponseEntity.status(httpServletResponse.getStatus()).body(errors);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}