package com.Sayed.Blog.Backend.Service.impl;


import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService
{
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public String generateToken(User user) {
        //System.out.println("Inside generateToken method");
        // Define expiration time (e.g., 10 minutes)
        long expirationTime = 60 * 24 * 60 * 1000; // 10 minutes in milliseconds
        Date now = new Date();
        //.setClock(() -> new Date(System.currentTimeMillis() - 5000)) // Allow 5 seconds skew
        Date expiryDate = new Date(now.getTime() + expirationTime);

        //System.out.println("Going to generate jwt token in generateToken method");
        String jwt= Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("Haneef")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(generateKey(),SignatureAlgorithm.HS384)
                .compact();
        //System.out.println("Generated jwt token in generateToken method="+ jwt);
        return jwt;
//        return "";
    }

//    @Override
//    public String generateToken(User user) {
//        //Map<String,Object> claims=new HashMap<>();
//        //LocalDateTime now =
//        System.out.println("generating token for user="+user.getEmail());
//        String token= Jwts.builder()   // This creates a new JWT builder instance using jjwt (JSON Web Token library), initializes a JWT object, allowing us to set various fields
//                //.setClaims(claims)  // ✅ Correct method   // initializes the claims section of the JWT.
//                .setSubject(user.getEmail())
//                .setIssuer("HANEEF")    //Helps in tracking which service generated the JWT.
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+60*10*1000))
//                // TODO -- expiry time ke according session check karna
//                .signWith(generateKey() , SignatureAlgorithm.HS384) //Signing ensures the token cannot be tampered with,Any modification to the JWT will make it invalid during verification
//                .compact(); //Generates the final compact JWT string.
//        System.out.println("Received JWT: " + token);
//        return token;
//        //return "";
//    }

    @Override
    public String extractUserName(String jwtToken) {
//        Claims claims=extractClaims(jwtToken);
        return extractClaims(jwtToken).getSubject();
    }

    @Override
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username= extractUserName(jwtToken);
        if(isTokenExpired(jwtToken)) return false;
        return (username.equals(userDetails.getUsername()));
    }



    private boolean isTokenExpired(String jwtToken) {
        Date date=extractClaims(jwtToken).getExpiration();
        return date.before((new Date()));
    }

    private Claims extractClaims(String jwtToken) {
       // System.out.println("Token before parsing: " + jwtToken);
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(generateKey()) // Use the secret key to validate the token
                .build()
                .parseClaimsJws(jwtToken)
                .getBody(); // Extract the claims from the token
        //System.out.println("Token parsed successfully!");
        return claims;
    }


    private SecretKey generateKey()
    {
        byte[] decode= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }
}
