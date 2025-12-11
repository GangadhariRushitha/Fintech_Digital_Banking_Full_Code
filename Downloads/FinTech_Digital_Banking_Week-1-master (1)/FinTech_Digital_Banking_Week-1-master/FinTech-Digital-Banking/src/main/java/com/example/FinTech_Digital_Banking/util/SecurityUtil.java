//package com.example.FinTech_Digital_Banking.util;
//
//import com.example.FinTech_Digital_Banking.Entity.User;
//import com.example.FinTech_Digital_Banking.Repository.UserRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SecurityUtil {
//
//    private final UserRepository userRepository;
//
//    public SecurityUtil(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    /**
//     * Returns the current authenticated user's id if available.
//     * Throws IllegalStateException if user not found.
//     */
//    public Long getCurrentUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new IllegalStateException("No authenticated user");
//        }
//
//        String principalName = auth.getName(); // typically the username/email
//        User user = userRepository.findByEmail(principalName)
//                .orElseThrow(() -> new IllegalStateException("User not found for email: " + principalName));
//        return user.getId();
//    }
//}
