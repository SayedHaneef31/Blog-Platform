package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface UserService {
     List<User> listUsers();

     User saveUser(User user);

     User getUserById(UUID id);
}
