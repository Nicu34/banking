package com.ing.banking.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

public class JwtTokenUtilTest {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER = "Authorization";
    private static final String TOKEN = "token";

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() {
        httpServletRequest = BDDMockito.mock(HttpServletRequest.class);
    }

    @ParameterizedTest
    @MethodSource("requestHeaderParameters")
    public void testIsValidRequestHeader(String requestHeaderParameterized, Boolean expectedResultParameterized) {
        // given
        jwtTokenUtil.setTokenHeaderName(HEADER);
        jwtTokenUtil.setBearerPrefix(BEARER_PREFIX);
        BDDMockito.given(httpServletRequest.getHeader(eq(HEADER))).willReturn(requestHeaderParameterized);

        // when
        Boolean actualResult = jwtTokenUtil.isValidRequestHeader(httpServletRequest);

        // then
        assertNotNull(actualResult);
        assertEquals(actualResult, expectedResultParameterized);
    }

    @Test
    public void testGetTokenFromRequestHeader() {
        // given
        jwtTokenUtil.setTokenHeaderName(HEADER);
        jwtTokenUtil.setBearerPrefix(BEARER_PREFIX);
        BDDMockito.given(httpServletRequest.getHeader(eq(HEADER))).willReturn(BEARER_PREFIX + TOKEN);

        // when
        String token = jwtTokenUtil.getTokenFromRequestHeader(httpServletRequest);

        // then
        assertNotNull(token);
        assertEquals(token, TOKEN);
    }

    @Test
    public void testGetNullTokenFromRequestHeader() {
        jwtTokenUtil.setTokenHeaderName(HEADER);
        BDDMockito.given(httpServletRequest.getHeader(eq(HEADER))).willReturn(null);

        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                jwtTokenUtil.getTokenFromRequestHeader(httpServletRequest));
    }

    private static Stream<Arguments> requestHeaderParameters() {
        return Stream.of(
                Arguments.of(null, Boolean.FALSE),
                Arguments.of("clearly not starting with Bearer string", Boolean.FALSE),
                Arguments.of("bearer still not valid my dear", Boolean.FALSE),
                Arguments.of("Bearer this format seems fine", Boolean.TRUE)
        );
    }
}