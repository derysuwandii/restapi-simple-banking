package com.derysuwandi.restapisimplebanking.repo;

import com.derysuwandi.restapisimplebanking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
