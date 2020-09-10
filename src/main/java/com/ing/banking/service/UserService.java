package com.ing.banking.service;

import com.ing.banking.converter.CredentialsToUserConverter;
import com.ing.banking.domain.User;
import com.ing.banking.exception.RegistrationException;
import com.ing.banking.model.RegistrationRequest;
import com.ing.banking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private CredentialsToUserConverter<RegistrationRequest> registerRequestToUserConverter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s doesn't exists.", username)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public void saveNewUser(RegistrationRequest registrationRequest) throws RegistrationException {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new RegistrationException(String.format("Username already exists: %s.", registrationRequest.getUsername()));
        }
        userRepository.save(registerRequestToUserConverter.convert(registrationRequest));
    }
}