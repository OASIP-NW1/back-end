
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
//               guest
                .anonymous().principal("guest").authorities("ROLE_guest").and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                all role
                .authorizeRequests()
//                see category role guest -> edit
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/match/**","/api/users/signup").hasRole("Admin")
//                admin
                .antMatchers(HttpMethod.GET,"/api/users/**","/api/match/**").hasRole("Admin")
//                request role
                .antMatchers(HttpMethod.GET, "/api/events","/api/events/{id}").hasAnyRole("Admin","Student","Lecturer","guest")
                .antMatchers(HttpMethod.POST, "/api/events").hasAnyRole("Admin","Student","guest")
                .antMatchers(HttpMethod.PUT, "/api/events/{id}").hasAnyRole("Admin","Student")
                .antMatchers(HttpMethod.DELETE, "/api/events/{id}").hasAnyRole("Admin","Student")
                .antMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("Admin")
                .antMatchers(HttpMethod.DELETE, "/api/events/file/{id}").hasAnyRole("Admin","Student")
                .antMatchers(HttpMethod.GET, "/api/refresh").permitAll()
//                eventcategory
                .antMatchers(HttpMethod.GET,"/api/eventCategory/**").hasAnyRole("Admin","guest","Student","Lecturer")
                .antMatchers(HttpMethod.PUT,"/api/eventCategory/**").hasRole("Admin");
//                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
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