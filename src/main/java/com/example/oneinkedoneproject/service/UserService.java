package com.example.oneinkedoneproject.service;


import com.example.oneinkedoneproject.domain.User;
import com.example.oneinkedoneproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Optional<User> findUserById(String id){

        return userRepository.findById(id);
    }
    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

}
