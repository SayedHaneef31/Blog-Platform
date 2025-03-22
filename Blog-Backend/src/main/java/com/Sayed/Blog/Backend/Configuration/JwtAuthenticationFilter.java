package com.Sayed.Blog.Backend.Configuration;

import com.Sayed.Blog.Backend.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;





@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;

        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("In doFilterInternal method");
        String authHeader=request.getHeader("Authorization");
        // Debugging - Check if the token is received
        System.out.println("authHeader before parsing: " + authHeader);

        if(authHeader== null || !authHeader.startsWith("Bearer "))   //Agar phle se autheticated nahi hoga iska matlab abhi tak jwt token nahi generate hua hoga
        {
            System.out.println("Token is null or does not start with 'Bearer '");
            filterChain.doFilter(request,response);
            return;
        }

        String jwt=authHeader.substring(7);
        System.out.println("Token after removing Bearer: " + jwt);
        final String username= jwtService.extractUserName(jwt);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();   //Checks if a user is already authenticated in this request.


        if(username != null && authentication== null)     //If no authentication exists (authentication == null), it means we need to validate the JWT and authenticate the user.
        {
            //Authenticate karo
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt,userDetails))
            {
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                /*
                     If the token is valid, creates a Spring Security authentication token (UsernamePasswordAuthenticationToken).
                     null: Since we are using JWT, there is no need to pass a password.
                     userDetails.getAuthorities(): Retrieves the user's roles/permissions.
                 */

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                ); //Attaches the authentication object to the SecurityContextHolder, marking the request as authenticated.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                //SecurityContextHolder is Spring Security’s way of storing authentication information.
                //It holds the security context for the current request, meaning it remembers who is authenticated for that request.
            }
        }
        filterChain.doFilter(request,response);


    }
}
