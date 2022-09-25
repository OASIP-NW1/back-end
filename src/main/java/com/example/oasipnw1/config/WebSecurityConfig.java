package com.example.oasipnw1.config;

import com.example.oasipnw1.customException.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService jwtUserDetailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtRequestFilter jwtRequestFilter;

    private final Argon2PasswordEncoder argon2PasswordEncoder;

    @Autowired
    public WebSecurityConfig(UserDetailsService jwtUserDetailsService, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter, Argon2PasswordEncoder argon2PasswordEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(argon2PasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
//                .exceptionHandling().accessDeniedHandler(new JwtAccessDenied()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/login").permitAll()
                .antMatchers("/api/users/signup").permitAll()
                .antMatchers("/api/users/**","/api/events/**","/api/match/**").hasRole("admin")
                .antMatchers("/api/events/**").hasRole("student")
//                .antMatchers("/api/events/**").hasRole()
//                .antMatchers("/api/refresh").permitAll()
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        //method we'll configure patterns to define protected/unprotected API endpoints. Please note that we have disabled CSRF protection because we are not using Cookies.
//
//
//        // We don't need CSRF for this example
//        httpSecurity.csrf().disable().cors().disable()
//                // dont authenticate this particular request
//                .authorizeRequests().antMatchers("/api/login").permitAll()
//                .antMatchers("/api/users/signup").permitAll() //user sign
//                // all other requests need to be authenticated
//                .anyRequest().authenticated().and().sessionManagement().
//                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//                and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                // make sure we use stateless session; session won't be used to
//                // store user's state.
////                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // Add a filter to validate the tokens with every request
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){

        return new CustomAccessDeniedHandler();
    }

}
