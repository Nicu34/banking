package com.ing.banking.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amount;

    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    private BankBranch bankBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
