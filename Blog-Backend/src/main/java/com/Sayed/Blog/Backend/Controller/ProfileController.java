package com.Sayed.Blog.Backend.Controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
@RequestMapping("/api/v1/user")
public class ProfileController {



    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(HttpServletRequest request) {
        // Log that the request has reached the backend
        //System.out.println("Received request at /user/profile");

        // Log all request headers
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            //System.out.println("Request Headers:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                //System.out.println("Header Name:"+headerName+" "+"Header Value:"+headerValue);

            }
        }
        // Log Authorization header specifically
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            System.out.println("Authorization Header: "+ authHeader);

        } else {
            System.out.println("Authorization Header is missing!");

        }

        return ResponseEntity.ok("User profile response");
    }
}
