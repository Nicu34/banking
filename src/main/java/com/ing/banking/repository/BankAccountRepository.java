package com.ing.banking.repository;

import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.BankAccount;
import com.ing.banking.domain.BankBranch;
import com.ing.banking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<List<BankAccount>> findAllByUser(User user);

    Optional<BankAccount> findAllByUserAndAccountType(User user, AccountType accountType);
}
