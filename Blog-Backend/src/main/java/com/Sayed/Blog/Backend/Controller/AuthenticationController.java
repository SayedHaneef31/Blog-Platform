package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        var userDetails = authenticationService.authenticate(email, password);
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
        String token = authenticationService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}
