package com.ing.banking;

import com.ing.banking.domain.BankBranch;
import com.ing.banking.domain.DayEnum;
import com.ing.banking.domain.WorkingDay;
import com.ing.banking.repository.BankBranchRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
/*
 * Class is used to populate in database a bank branch with its working hours & days, alongside its offset.
 * In my example, I created a bank branch with working hours between 9-17, working days from Monday to Friday
 */
public class DataLoader implements ApplicationRunner {

    private BankBranchRepository bankBranchRepository;

    @Override
    public void run(ApplicationArguments args) {
        bankBranchRepository.save(buildMockedBankBranch());
    }

    private BankBranch buildMockedBankBranch() {
        int bankOpeningHour = 9;
        int bankClosingHour = 17;

        return BankBranch.builder()
                .location("Romania")
                .openingTime(LocalTime.of(bankOpeningHour, 0))
                .closingTime(LocalTime.of(bankClosingHour, 0))
                .workingDay(buildWorkingDays())
                .build();
    }

    private List<WorkingDay> buildWorkingDays() {
        return Stream.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY")
                .map(DayEnum::valueOf)
                .map(this::buildWorkingDay)
                .collect(Collectors.toList());
    }

    private WorkingDay buildWorkingDay(DayEnum dayEnum) {
        return WorkingDay.builder()
                .day(dayEnum)
                .build();
    }
}
