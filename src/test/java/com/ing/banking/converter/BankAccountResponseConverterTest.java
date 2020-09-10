package com.ing.banking.converter;

import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.BankAccount;
import com.ing.banking.model.BankAccountResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BankAccountResponseConverterTest {

    private static final double AMOUNT = 66.66;

    @Test
    void convert() {
        BankAccountResponseConverter converter = new BankAccountResponseConverter();
        BankAccount bankAccount = mockBankAccount();

        BankAccountResponse bankAccountResponse = converter.convert(bankAccount);

        assertNotNull(bankAccountResponse);
        assertEquals(AMOUNT, bankAccountResponse.getAmount());
        assertEquals(AccountType.SAVINGS, bankAccountResponse.getAccountType());
    }

    private BankAccount mockBankAccount() {
        BankAccount bankAccount = mock(BankAccount.class);
        when(bankAccount.getAccountType())
                .thenReturn(AccountType.SAVINGS);
        when(bankAccount.getAmount())
                .thenReturn(AMOUNT);

        return bankAccount;
    }
}