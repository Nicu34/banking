package com.ing.banking.util;

import com.ing.banking.domain.*;
import com.ing.banking.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class BankAccountRequirementsValidatorTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountRequirementsValidator validator;

    private BankBranch bankBranch;

    @BeforeEach
    void setUp() {
        initMocks(this);
        bankBranch = mockBankBranch();
    }

    @ParameterizedTest
    @MethodSource("validTimeParameters")
    void isWithinWorkingHours(String time) {
        LocalTime assumedTime = LocalTime.parse(time);
        assertTrue(validator.isWithinWorkingHours(assumedTime, bankBranch.getOpeningTime(), bankBranch.getClosingTime()));
    }

    @Test
    void isWorkingDay() {
        // 12 December 2020 (Tuesday)
        LocalDateTime assumedDateTime = LocalDateTime.parse("2020-12-22T10:15:30");
        assertTrue(validator.isWorkingDay(bankBranch.getWorkingDay(), assumedDateTime));
    }

    @Test
    void userHasSavingsAccount() {
        User user = mock(User.class);
        when(bankAccountRepository.findAllByUserAndAccountType(eq(user), eq(AccountType.SAVINGS)))
                .thenReturn(Optional.of(new BankAccount()));

        assertTrue(validator.userHasSavingsAccount(user));
    }

    @ParameterizedTest
    @MethodSource("invalidTimeParameters")
    void isNotWithinWorkingHours(String time) {
        LocalTime assumedTime = LocalTime.parse(time);
        assertFalse(validator.isWithinWorkingHours(assumedTime, bankBranch.getOpeningTime(), bankBranch.getClosingTime()));
    }

    @Test
    void userDoesNotHaveSavingAccount() {
        User user = mock(User.class);
        when(bankAccountRepository.findAllByUserAndAccountType(eq(user), eq(AccountType.SAVINGS)))
                .thenReturn(Optional.empty());

        assertFalse(validator.userHasSavingsAccount(user));
    }

    @Test
    void isNotWorkingDay() {
        // 10 December 2020 (Sunday)
        LocalDateTime assumedDateTime = LocalDateTime.parse("2020-10-22T10:15:30");
        assertFalse(validator.isWorkingDay(bankBranch.getWorkingDay(), assumedDateTime));
    }

    private BankBranch mockBankBranch() {
        BankBranch bankBranch = mock(BankBranch.class);
        LocalTime openingTime =  LocalTime.parse("09:00");
        LocalTime closingTime =  LocalTime.parse("18:00");
        List<WorkingDay> workingDays = buildWorkingDays();

        when(bankBranch.getOpeningTime())
                .thenReturn(openingTime);
        when(bankBranch.getClosingTime())
                .thenReturn(closingTime);
        when(bankBranch.getWorkingDay())
                .thenReturn(workingDays);

        return bankBranch;
    }

    private List<WorkingDay> buildWorkingDays() {
        return Stream.of(DayEnum.MONDAY, DayEnum.TUESDAY, DayEnum.WEDNESDAY, DayEnum.THURSDAY)
                .map(this::buildWorkingDay)
                .collect(Collectors.toList());
    }

    private WorkingDay buildWorkingDay(DayEnum dayEnum) {
        return WorkingDay.builder()
                .day(DayEnum.TUESDAY)
                .build();
    }

    private static Stream<Arguments> validTimeParameters() {
        return Stream.of(
                Arguments.of("12:15:00"),
                Arguments.of("09:00:15"),
                Arguments.of("17:59:40")
        );
    }

    private static Stream<Arguments> invalidTimeParameters() {
        return Stream.of(
                Arguments.of("09:00:00"),
                Arguments.of("18:00:00"),
                Arguments.of("08:59:00"),
                Arguments.of("20:59:40")
        );
    }

}