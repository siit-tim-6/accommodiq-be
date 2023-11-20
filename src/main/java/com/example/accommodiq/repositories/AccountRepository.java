package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Long, Account> {
}
