package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {


    String extractUserName(String jwt);

    boolean isTokenValid(String jwt, UserDetails userDetails);

    String generateToken(User user);
}
