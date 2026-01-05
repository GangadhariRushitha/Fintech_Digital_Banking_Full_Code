package com.example.FinTech_Digital_Banking.Controller;

import com.example.FinTech_Digital_Banking.Configuration.JwtServices;
import com.example.FinTech_Digital_Banking.DTO.JwtResponse;
import com.example.FinTech_Digital_Banking.DTO.LoginRequest;
import com.example.FinTech_Digital_Banking.DTO.RegisterRequest;
import com.example.FinTech_Digital_Banking.Entity.User;
import com.example.FinTech_Digital_Banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin()
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtServices jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        
         if (repo.findByEmail(req.getEmail()).isPresent()) {
        return "Email already registered!";
    }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
   
        repo.save(user);

        return "User registered successfully";
    }
    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                  req.getPassword()
                )
        );

        String token = jwtService.generateToken(req.getEmail());
        return new JwtResponse(token);
    }
}








