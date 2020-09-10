package com.ing.banking.service;

import com.ing.banking.authentication.JwtTokenUtil;
import com.ing.banking.converter.BankAccountRequestConverter;
import com.ing.banking.converter.BankAccountResponseConverter;
import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.BankAccount;
import com.ing.banking.domain.BankBranch;
import com.ing.banking.domain.User;
import com.ing.banking.exception.BankAccountException;
import com.ing.banking.model.BankAccountRequest;
import com.ing.banking.model.BankAccountResponse;
import com.ing.banking.repository.BankAccountRepository;
import com.ing.banking.repository.BankBranchRepository;
import com.ing.banking.repository.UserRepository;
import com.ing.banking.util.BankAccountRequirementsValidator;
import com.ing.banking.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BankingService {

    private BankAccountRepository bankAccountRepository;

    private UserRepository userRepository;

    private BankAccountRequestConverter bankAccountRequestConverter;

    private BankAccountResponseConverter bankAccountResponseConverter;

    private BankBranchRepository bankBranchRepository;

    private JwtTokenUtil jwtTokenUtil;

    private BankAccountRequirementsValidator bankingCriteriaValidator;

    /**
     * Creates a new banking account for the user. User is extracted from the authorization token.
     * Received OffsetDateTime is converted to LocalDateTime using UTC offset
     * because the date time of the Bank Branch is saved using UTC time into the database.
     * @throws BankAccountException - If the creation date time of the savings bank account is not within working hours
     * or in a working day of the bank branch or if a savings bank account type is already present for the user.
     */
    public void createNewBankAccount(BankAccountRequest request) throws BankAccountException {
        BankBranch bankBranch = bankBranchRepository.findById(request.getBankBranchId())
                .orElseThrow(() -> new BankAccountException("A bank account should be opened within a bank branch."));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Authentication couldn't be performed"));
        LocalDateTime creationDateTime = OffsetDateTime
                .parse(request.getCreationDateTime(), DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT))
                .withOffsetSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        if (AccountType.SAVINGS.name().equals(request.getAccountType())) {
            validateBankAccountRequirements(user, bankBranch, creationDateTime);
        }
        BankAccount bankAccount = bankAccountRequestConverter.convert(request.getAccountType(), user);
        bankAccountRepository.save(bankAccount);
    }

    /**
     * Returns created bank account for a user.
     */
    public List<BankAccountResponse> retrieveBankAccountsForUser(HttpServletRequest request) {
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Authentication couldn't be performed"));

        List<BankAccount> bankAccounts = bankAccountRepository.findAllByUser(user)
                .orElse(Collections.emptyList());

        return bankAccountResponseConverter.convertFrom(bankAccounts);
    }

    /**
     * Validates the requirements for creation of a bank account of type savings.
     * - a user is allowed to create only one savings type bank account
     * - a bank account should be opened within working hours of the agency
     * - a bank account should be opened in a working day of the agency
     * @param user - User which intends to open a bank account
     * @param bankBranch - Bank branch within a bank account should be opened
     * @param creationDateTime - Creation date time of a bank account, already converter to UTC time.
     * @throws BankAccountException - If one of the requirements is not accomplished.
     */
    private void validateBankAccountRequirements(User user, BankBranch bankBranch, LocalDateTime creationDateTime) throws BankAccountException {
        if (bankingCriteriaValidator.userHasSavingsAccount(user)) {
            throw new BankAccountException("A secondary saving bank account cannot be opened. Limit is one saving account per user.");
        }
        if (!bankingCriteriaValidator.isWorkingDay(bankBranch.getWorkingDay(), creationDateTime)) {
            throw new BankAccountException("A bank account should be opened in one of the working days of the bank branch.");
        }
        if (!bankingCriteriaValidator.isWithinWorkingHours(creationDateTime.toLocalTime(), bankBranch.getOpeningTime(),
                bankBranch.getClosingTime())) {
            throw new BankAccountException("A bank account should be opened within working hours of the bank branch.");
        }
    }
}
