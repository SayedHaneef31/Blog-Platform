package com.Sayed.Blog.Backend.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.and()) // Use the existing CORS configuration
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/v1/auth/**").permitAll() // Allow authentication APIs (login/register)
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll() // Allow public read access to posts
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll() // Allow public read access to categories
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll() // Allow public read access to tags
                        .requestMatchers("/api/v1/user/profile").authenticated() // User profile requires authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts/**").authenticated() // Create posts requires authentication
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").authenticated() // Update posts requires authentication
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").authenticated() // Delete posts requires authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").authenticated() // Create categories requires authentication
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").authenticated() // Update categories requires authentication
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").authenticated() // Delete categories requires authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/tags/**").authenticated() // Create tags requires authentication
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tags/**").authenticated() // Delete tags requires authentication
                        .anyRequest().authenticated() // Require authentication for any other requests
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();

    }

}
