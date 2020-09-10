package com.ing.banking.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class JwtAuthorizationTokenFilterTest {

    private static final String TOKEN = "this is an auth token";
    private static final String USERNAME = "cool username";

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;

    private HttpServletRequest httpServletRequest = spy(HttpServletRequest.class);

    private HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    private FilterChain filterChain = mock(FilterChain.class);

    @BeforeEach
    public void setUp() throws IOException, ServletException {
        MockitoAnnotations.initMocks(this);
        given(jwtTokenUtil.isValidRequestHeader(any())).willReturn(Boolean.TRUE);
        given(jwtTokenUtil.getTokenFromRequestHeader(eq(httpServletRequest))).willReturn(TOKEN);
        doNothing().when(filterChain).doFilter(eq(httpServletRequest), eq(httpServletResponse));
    }

    @ParameterizedTest
    @MethodSource("isTokenValidParameters")
    public void testDoFilterInternal(Boolean isTokenValid) throws ServletException, IOException {
        given(jwtTokenUtil.getUsernameFromToken(anyString())).willReturn(USERNAME);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(mock(UserDetails.class));
        given(jwtTokenUtil.isTokenValidForUser(anyString(), any(UserDetails.class))).willReturn(isTokenValid);

        jwtAuthorizationTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(jwtTokenUtil).getUsernameFromToken(anyString());
        verify(jwtTokenUtil).isTokenValidForUser(anyString(), any(UserDetails.class));
    }

    @Test
    public void testDoFilterInternalWithNullUsername() throws IOException, ServletException {
        given(jwtTokenUtil.getUsernameFromToken(anyString())).willReturn(null);

        jwtAuthorizationTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(jwtTokenUtil).getUsernameFromToken(anyString());
    }

    private static Stream<Arguments> isTokenValidParameters() {
        return Stream.of(
                Arguments.of(false),
                Arguments.of(true)
        );
    }

}