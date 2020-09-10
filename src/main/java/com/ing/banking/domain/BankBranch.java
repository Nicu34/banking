package com.ing.banking.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String location;

    @OneToMany(fetch = FetchType.EAGER)
    @Cascade(value = CascadeType.PERSIST)
    private List<WorkingDay> workingDay;

    private String name;
    private LocalTime openingTime;
    private LocalTime closingTime;

}
