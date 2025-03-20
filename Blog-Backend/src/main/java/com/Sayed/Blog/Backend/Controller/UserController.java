package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController
{

    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> listAllUsers()
    {
        return  userService.listUsers();
    }
//
//    @PostMapping
//    public ResponseEntity<User> UserLogin(@RequestBody User user)
//    {
//
//
//    }

}
