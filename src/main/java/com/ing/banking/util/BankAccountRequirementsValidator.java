package com.ing.banking.util;

import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.DayEnum;
import com.ing.banking.domain.User;
import com.ing.banking.domain.WorkingDay;
import com.ing.banking.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class BankAccountRequirementsValidator {

    private BankAccountRepository bankAccountRepository;

    /**
     * @return true if the user has a savings bank account, false otherwise
     */
    public boolean userHasSavingsAccount(User user) {
        return bankAccountRepository.findAllByUserAndAccountType(user, AccountType.SAVINGS).isPresent();
    }

    /**
     *
     * @param workingDays - Working days of the bank branch
     * @param creationDateTime - creation date time of the bank account
     * @return true if the creation date is one of the working days, false otherwise
     */
    public boolean isWorkingDay(List<WorkingDay> workingDays, LocalDateTime creationDateTime) {
        int bankAccountDayValue = creationDateTime.getDayOfWeek()
                .getValue();

        Optional<Integer> matchedValueOfTheDay = workingDays
                .stream()
                .map(WorkingDay::getDay)
                .map(DayEnum::getDayOfTheWeekValue)
                .filter(d -> bankAccountDayValue == d)
                .findFirst();

        return matchedValueOfTheDay.isPresent();
    }

    /**
     *
     * @param creationDateTime - creation date time of the bank account
     * @param openingTime - opening time of the bank branch
     * @param closingTime - closing time of the bank branch
     * @return true if the creation time is within opening and closing time, false otherwise
     */
    public boolean isWithinWorkingHours(LocalTime creationDateTime, LocalTime openingTime, LocalTime closingTime) {
        return creationDateTime.isAfter(openingTime) && creationDateTime.isBefore(closingTime);
    }
}
