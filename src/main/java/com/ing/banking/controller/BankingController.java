package com.ing.banking.controller;

import com.ing.banking.exception.BankAccountException;
import com.ing.banking.model.BankAccountRequest;
import com.ing.banking.model.BankAccountResponse;
import com.ing.banking.service.BankingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/banking")
@Api(tags = "Banking account creation and retrieval.")
public class BankingController {

    private BankingService bankingService;

    @PostMapping(value = "/createAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Banking account creation for user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created bank account."),
            @ApiResponse(code = 401, message = "Authentication couldn't be performed"),
            @ApiResponse(code = 409, message = "Bank account not created because of the requirements." +
                    " A bank account must be opened within working hours and in the working day of the bank branch." +
                    "Only one bank account of type savings is allowed per user."),
    })
    public void createBankingAccount(@RequestBody @Valid BankAccountRequest request) throws BankAccountException {
        bankingService.createNewBankAccount(request);
    }

    @ApiOperation(value = "Retrieves bank accounts for user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns created bank accounts for user"),
            @ApiResponse(code = 401, message = "Authentication couldn't be performed"),
    })
    @GetMapping(value = "/getAccounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BankAccountResponse>> getBankAccountsForUser(HttpServletRequest request) {
        return ResponseEntity.ok(bankingService.retrieveBankAccountsForUser(request));
    }
}
