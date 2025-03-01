package com.Sayed.Blog.Backend.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.config.AuditingConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig
{
//    SecurityFilterChain handles which requests require authentication and how they’re secured.
    @Bean                  //Every configuration class has some bean defined
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/tags/**").permitAll()
                        .anyRequest().authenticated()

                )
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                 //Tells Spring Security not to create or use HTTP sessions
        return http.build();

    }

//    PasswordEncoder ensures secure password hashing.
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    AuthenticationManager is the core for authenticating user credentials.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }




}
