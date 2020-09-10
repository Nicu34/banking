package com.ing.banking.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JwtAuthenticationEntryPointTest {

    @Test
    public void testCommence() throws IOException {
        // given
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        AuthenticationException authenticationException = mock(AuthenticationException.class);

        // when
        jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, authenticationException);

        // then
        verify(httpServletResponse).sendError(eq(HttpServletResponse.SC_UNAUTHORIZED));
    }

}