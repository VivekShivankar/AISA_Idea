package com.aisaidea.service;

import com.aisaidea.model.User;
import com.aisaidea.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(String username, String email, String password, String fullName) {
        if (userRepository.existsByUsername(username))
            throw new RuntimeException("Username already taken");
        if (userRepository.existsByEmail(email))
            throw new RuntimeException("Email already registered");
        User u = new User();
        u.setUsername(username.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setPassword(passwordEncoder.encode(password));
        u.setFullName(fullName != null ? fullName.trim() : username);
        return userRepository.save(u);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void uploadImage(String username, MultipartFile file) throws Exception {
        User u = userRepository.findByUsername(username).orElseThrow();
        String b64 = "data:" + file.getContentType() + ";base64,"
            + Base64.getEncoder().encodeToString(file.getBytes());
        u.setProfileImageBase64(b64);
        userRepository.save(u);
    }
}
