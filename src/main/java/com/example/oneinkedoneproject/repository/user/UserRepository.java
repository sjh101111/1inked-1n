package com.example.oneinkedoneproject.repository.user;

import com.example.oneinkedoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface
UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<List<User>> findAllByIdentityAndDescription(String keyword1, String keyword2);
}