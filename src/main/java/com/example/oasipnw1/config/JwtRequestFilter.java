package com.example.oasipnw1.config;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.oasipnw1.services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Setter
    @Getter
    String newToken;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Getter @Setter
    UserDetails userDetails;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
         String username = null;


//        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            setNewToken(requestTokenHeader.substring(7));
            try {
                JSONObject payload = null;
                if (getNewToken() != null) {
                    payload = extractMSJwt(getNewToken());
                }


                System.out.println(username);

                if (StringUtils.hasText(getNewToken()) == true && payload.getString("iss").equals("https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d/v2.0")) {
                    System.out.println("MSIP");
                    username = payload.getString("preferred_username");
                    if(payload.has("roles")==false){
                        setUserDetails(new User(payload.getString("preferred_username"),"",Arrays.asList(new SimpleGrantedAuthority("ROLE_guest"))));
                        setNewToken(jwtTokenUtil.generateToken(getUserDetails()));
                    }else {
                        String role = payload.getString("roles");
                        String extract = role.replaceAll("[^a-zA-Z]+", "");
                        setUserDetails( new User(payload.getString("preferred_username"),"",Arrays.asList(new SimpleGrantedAuthority("ROLE_"+extract))));
//                    if(payload.has("role") == false){
//                        System.out.println("skdjf");
//                        setNewToken(jwtTokenUtil.generateToken("Guest", payload.getString("preferred_username"),payload.getString("name")).getAccessToken());
//                        System.out.println(getNewToken());
//                    }else
//                    {
                        setNewToken(jwtTokenUtil.generateToken(getUserDetails()));

                    }

                    System.out.println(getNewToken());
//                    }

                }else if (StringUtils.hasText(getNewToken()) == true && payload.getString("iss").equals("https://intproj21.sit.kmutt.ac.th/kw1/")) {
                    username = payload.getString("sub");
                    setUserDetails(this.jwtUserDetailsService.loadUserByUsername(username));
                }



            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                String requestURL = request.getRequestURL().toString();
                System.out.println(requestURL);
                if(requestURL.contains("refresh")){
                    request.setAttribute("Errors", "Refresh token was expired. Please make a new signin request");
                }else{
                    request.setAttribute("Errors", e.getMessage());
                }
            }catch (MalformedJwtException e){
                request.setAttribute("Errors", e.getMessage());
            }catch (SignatureException e){
                request.setAttribute("Errors", e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            request.setAttribute("Errors", "Token doesn't exist");
        }
        System.out.println(username);
        System.out.println(username != null);
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(getNewToken(), getUserDetails())) {
                System.out.println("test1");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        getUserDetails(), null, getUserDetails().getAuthorities());
                String authorities = getUserDetails().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
                System.out.println("Authorities granted " + authorities);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
    @SneakyThrows
    public JSONObject extractMSJwt(String token) {
        String[] chunks = token.split("\\.");

        JSONObject header = new JSONObject(decode(chunks[0]));
        JSONObject payload = new JSONObject(decode(chunks[1]));
        String signature = decode(chunks[2]);
        if (payload.getString("iss").equals
                ("https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d/v2.0")) {
            System.out.println("BEFORE CONFIG");

            DecodedJWT jwt = JWT.decode(token);
            JwkProvider provider = new UrlJwkProvider(
                    new URL("https://login.microsoftonline.com/common/discovery/keys"));
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            System.out.println("AFTER CONFIG");
        }
        System.out.println("PAYLOAD : " + payload);
        return payload;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}