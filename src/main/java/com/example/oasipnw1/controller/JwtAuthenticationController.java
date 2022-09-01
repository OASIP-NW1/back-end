//package com.example.oasipnw1.controller;
//
//import com.example.oasipnw1.config.JwtTokenUtil;
//import com.example.oasipnw1.model.JwtRequest;
//import com.example.oasipnw1.model.JwtResponse;
//import com.example.oasipnw1.repository.UserRepository;
//import com.example.oasipnw1.services.JwtUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin
//
//public class JwtAuthenticationController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtUserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//    public ResponseEntity<?> createAuthenticationToken(
//            @RequestBody JwtRequest authenticationRequest) throws Exception {
//
//        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getEmail());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }
//}
//
