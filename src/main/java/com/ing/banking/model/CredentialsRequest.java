package com.ing.banking.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsRequest implements CredentialsInterface {
    @NotEmpty(message = "Username cannot be empty.")
    private String username;
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
}