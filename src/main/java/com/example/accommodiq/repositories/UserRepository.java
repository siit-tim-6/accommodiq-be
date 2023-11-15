package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
