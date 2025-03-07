package com.Sayed.Blog.Backend.Security;

import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService
{

    private final UserRepo userRepo;

    public BlogUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepo.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with email="+ email));
        return new BlogUserDetails(user);
    }
}
