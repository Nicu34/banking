package com.ing.banking.converter;

import com.ing.banking.domain.User;
import com.ing.banking.model.CredentialsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class CredentialsToUserConverterTest {

    private static final String PASSWORD = "so encoded password";
    private static final String USERNAME = "username";

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CredentialsToUserConverter<CredentialsRequest> converter;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void convert() {
        when(passwordEncoder.encode(anyString()))
                .thenReturn(PASSWORD);

        User user = converter.convert(mockCredentialsRequest());

        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(PASSWORD, user.getPassword());
    }

    private CredentialsRequest mockCredentialsRequest() {
        return CredentialsRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }
}