package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }
}
