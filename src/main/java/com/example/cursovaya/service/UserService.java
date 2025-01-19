package com.example.cursovaya.service;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Role;
import com.example.cursovaya.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);  // По умолчанию роль пользователя будет USER
        return userRepository.save(user);
    }

    public boolean register(String username, String FirstName, String LastName, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return false; // Пользователь с таким email уже существует
        }

        User user = new User();
        user.setUsername(username);
        user.setFirstName(FirstName);
        user.setLastName(LastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String username){
        userRepository.delete(findByUsername(username));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
