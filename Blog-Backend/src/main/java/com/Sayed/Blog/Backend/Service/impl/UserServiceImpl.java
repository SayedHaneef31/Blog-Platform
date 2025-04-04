package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @Override
    public User saveUser(User user) {
        System.out.println("In User service impl");
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // Encode before saving the password
        return userRepo.save(user);
        //return userRepo.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepo.findById(id).orElseThrow(()-> new UsernameNotFoundException("User with id:"+id+" not found"));
    }
}
