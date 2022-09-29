package com.example.oasipnw1.config;

import com.example.oasipnw1.customException.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .authorizeRequests()
                .antMatchers("/api/login","/api/users/signup").permitAll()
                .antMatchers("/api/users/**","/api/match/**").hasRole("admin")
<<<<<<< HEAD
//                .antMatchers("/api/events/**").hasRole("student")
                .antMatchers("/api/events/**").hasRole("admin")
                .antMatchers("/api/events/{id}").hasRole("student")
//                .antMatchers("api/eventCategory/**").hasRole("lecturer")
//                .antMatchers("/api/refresh").permitAll()
=======
                .antMatchers(HttpMethod.GET, "/api/events","/api/events/{id}").hasAnyRole("admin","student","lecturer")
                .antMatchers(HttpMethod.POST, "/api/events").hasAnyRole("admin","student","guest")
                .antMatchers(HttpMethod.PUT, "/api/events/{id}").hasAnyRole("admin","student")
                .antMatchers(HttpMethod.DELETE, "/api/events/{id}").hasAnyRole("admin","student")
>>>>>>> fe516da694cbe849c0152bf9e947d00f77dd3146
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//                .antMatchers("/api/events/**").hasRole("student")
//                .antMatchers("/api/events/**").access("hasRole('admin') or hasRole('student')")
//                .antMatchers("/api/refresh").permitAll()

//        method we'll configure patterns to define protected/unprotected API endpoints. Please note that we have disabled CSRF protection because we are not using Cookies.
//
//
//        We don't need CSRF for this example
//        httpSecurity.csrf().disable().cors().disable()
//                dont authenticate this particular request
//                .authorizeRequests().antMatchers("/api/login").permitAll()
//                .antMatchers("/api/users/signup").permitAll() //user sign
//                all other requests need to be authenticated
//                .anyRequest().authenticated().and().sessionManagement().
//                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//                and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                make sure we use stateless session; session won't be used to
//                store user's state.
//                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
<<<<<<< HEAD
//        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
=======
//        Add a filter to validate the tokens with every request
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
>>>>>>> fe516da694cbe849c0152bf9e947d00f77dd3146
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