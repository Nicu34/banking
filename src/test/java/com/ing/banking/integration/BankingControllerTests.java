package com.ing.banking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.banking.model.BankAccountRequest;
import com.ing.banking.model.CredentialsRequest;
import com.ing.banking.model.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BankingControllerTests {

    private static final String USERNAME = "client";
    private static final String PASSWORD = "clientPassword";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    String authenticationToken;

    @BeforeEach
    void createUserAndAuthenticate() throws Exception {
        register();
        authenticationToken = logIn();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addSavingsBankAccount() throws Exception {
        createSavingsBankAccount();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addMultipleSavingsBankAccount() throws Exception {
        createSavingsBankAccount();
        createExtraSavingsBankAccount();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createSavingsBankAccountInvalidTime() throws Exception {
        BankAccountRequest request = BankAccountRequest.builder()
                .accountType("SAVINGS")
                .creationDateTime("2020-09-10 19:30:00 +02")
                .bankBranchId(1)
                .userId(1)
                .build();

        mockMvc.perform(
                post("/banking/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authenticationToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createSavingsBankAccountInvalidDay() throws Exception {
        BankAccountRequest request = BankAccountRequest.builder()
                .accountType("SAVINGS")
                .creationDateTime("2020-09-12 12:30:00 +02")
                .bankBranchId(1)
                .userId(1)
                .build();

        mockMvc.perform(
                post("/banking/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authenticationToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createSavingsBankAccountInvalidInputDate() throws Exception {
        BankAccountRequest request = BankAccountRequest.builder()
                .accountType("SAVINGS")
                .creationDateTime("2020-09-10 12:00")
                .bankBranchId(1)
                .userId(1)
                .build();

        mockMvc.perform(
                post("/banking/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authenticationToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private void register() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .confirmationPassword(PASSWORD)
                .build();

        mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private String logIn() throws Exception {
        CredentialsRequest request = CredentialsRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        return mockMvc.perform(
                post("/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void createSavingsBankAccount() throws Exception {
        BankAccountRequest request = BankAccountRequest.builder()
                .accountType("SAVINGS")
                .creationDateTime("2020-09-10 12:00:00 +02")
                .bankBranchId(1)
                .userId(1)
                .build();

        mockMvc.perform(
                post("/banking/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authenticationToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private void createExtraSavingsBankAccount() throws Exception {
        BankAccountRequest request = BankAccountRequest.builder()
                .accountType("SAVINGS")
                .creationDateTime("2020-09-10 12:00:00 +02")
                .bankBranchId(1)
                .userId(1)
                .build();

        mockMvc.perform(
                post("/banking/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authenticationToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
