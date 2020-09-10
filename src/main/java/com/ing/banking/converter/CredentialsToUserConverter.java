package com.ing.banking.converter;

import com.ing.banking.domain.User;
import com.ing.banking.model.CredentialsInterface;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CredentialsToUserConverter<T extends CredentialsInterface> implements Converter<T, User> {

    private PasswordEncoder passwordEncoder;

    @Override
    @NonNull
    public User convert(T credentials) {
        return User.builder()
                .username(credentials.getUsername())
                .password(passwordEncoder.encode(credentials.getPassword()))
                .build();
    }
}
