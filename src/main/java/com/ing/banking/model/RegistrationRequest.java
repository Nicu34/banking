package com.ing.banking.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class RegistrationRequest implements CredentialsInterface {
    @NotEmpty(message = "Username cannot be empty.")
    private String username;
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
    @NotEmpty(message = "Confirmation password cannot be empty.")
    private String confirmationPassword;
}
