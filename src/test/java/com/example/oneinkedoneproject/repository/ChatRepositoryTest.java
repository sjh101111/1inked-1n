package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.repository.ChatRepository;
import com.example.oneinkedoneproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;
}
