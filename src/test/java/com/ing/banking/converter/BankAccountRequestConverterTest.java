package com.ing.banking.converter;

import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.BankAccount;
import com.ing.banking.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountRequestConverterTest {

    @Test
    void convert() {
        BankAccountRequestConverter converter = new BankAccountRequestConverter();
        String accountType = AccountType.OTHER.name();
        User user = Mockito.mock(User.class);

        BankAccount bankAccount = converter.convert(accountType, user);

        assertNotNull(bankAccount);
        assertEquals(AccountType.OTHER, bankAccount.getAccountType());
        assertEquals(user, bankAccount.getUser());
    }
}