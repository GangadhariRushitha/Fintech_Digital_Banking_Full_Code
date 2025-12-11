//package com.example.FinTech_Digital_Banking.Service;
//
//import com.example.FinTech_Digital_Banking.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.Optional;
//
//@Service
//public class AuthService {
//    @Autowired private UserRepository userRepository;
//    public boolean authenticate(String username, String password){
//        Optional<com.example.FinTech_Digital_Banking.Entity.User> u = userRepository.findByUsername(username);
//        return u.filter(user -> user.getPassword().equals(password)).isPresent();
//    }
//}