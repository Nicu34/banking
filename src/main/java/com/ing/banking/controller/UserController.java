package com.ing.banking.controller;

import com.ing.banking.authentication.JwtTokenUtil;
import com.ing.banking.exception.RegistrationException;
import com.ing.banking.model.CredentialsRequest;
import com.ing.banking.model.RegistrationRequest;
import com.ing.banking.repository.UserRepository;
import com.ing.banking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Api(tags = "User registration and authentication")
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "User registration")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully registered user."),
            @ApiResponse(code = 409, message = "Unsuccessfully user registration. Username already exists."),
    })
    public void register(@RequestBody @Valid  RegistrationRequest request) throws RegistrationException {
        userService.saveNewUser(request);
    }

    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "User authentication", response = String.class,
            notes = "Returns an authorization token which should be used on Authorization header with the value \"Bearer <authorizationToken>\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved authorization token"),
            @ApiResponse(code = 401, message = "Authentication couldn't be performed."),
    })
    public ResponseEntity<String> authenticate(@RequestBody @Valid CredentialsRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final String token = jwtTokenUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(token);
    }
}
