package com.derysuwandi.restapisimplebanking.repo;

import com.derysuwandi.restapisimplebanking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
