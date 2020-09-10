package com.ing.banking.repository;

import com.ing.banking.domain.BankBranch;
import com.ing.banking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, Integer> {
}
