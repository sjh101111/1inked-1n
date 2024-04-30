package com.example.oneinkedoneproject.repository;

import com.example.oneinkedoneproject.repository.FollowRepository;
import com.example.oneinkedoneproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;
}
