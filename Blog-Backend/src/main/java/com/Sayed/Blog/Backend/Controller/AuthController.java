package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.DTO.LoginUserDto;
import com.Sayed.Blog.Backend.Entity.DTO.RegisterUserDto;
import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Security.BlogUserDetails;
import com.Sayed.Blog.Backend.Service.JwtService;
import com.Sayed.Blog.Backend.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController
{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/get-users")
    public List<User> listAllUsers()
    {
        return  userService.listUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<User> UserRegister(@RequestBody RegisterUserDto registerUserDto)
    {
        System.out.println("In register post method");
        if (userRepo.existsByEmail(registerUserDto.getEmail())) {
            throw new DuplicateKeyException("Email already exists with email! "+registerUserDto.getEmail());
        }
        System.out.println("In register post method with different email");
        User user=new User();
        user.setEmail(registerUserDto.getEmail());
        user.setName(registerUserDto.getName());
        user.setPassword(registerUserDto.getPassword());

        return ResponseEntity.ok(userService.saveUser(user));



    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto)
    {

        try {
            System.out.println("I am inside login post method");
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));

            System.out.println("Given the details to authentication manager");
            BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser(); // Assuming BlogUserDetails has a getUser() method

            System.out.println("Fetched authenticated user details");

                System.out.println("Authentication successful now going to generate the jwt token");
                String token = jwtService.generateToken(user);
                System.out.println("Token recived after successfull authentication:" + token);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Set token in header
                        .body(Map.of(
                                "token", token,
                                "message", "Login successful"
                        ));


        }catch (BadCredentialsException e) {
            System.out.println("Invalid credentials: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
        catch(Exception e)
        {
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong"));
        }


    }



}
