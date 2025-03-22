package com.Sayed.Blog.Backend.Security;

import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//this is needed by spring security to fetch user from db
@Service
public class BlogUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public BlogUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with email"+email));

        return new BlogUserDetails(user);

    }
}
