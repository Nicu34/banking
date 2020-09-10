package com.ing.banking.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Setter
public class JwtTokenUtil implements Serializable {

    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Value("${jwt.logged.in.expiration.days}")
    private Integer loggedInExpirationDays;

    @Value("${jwt.header}")
    private String tokenHeaderName;

    @Value("${jwt.bearer.prefix}")
    private String bearerPrefix;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequestHeader(request);

        return getUsernameFromToken(token);
    }

    public String getTokenFromRequestHeader(HttpServletRequest request) {
        final Optional<String> authenticationToken = Optional.ofNullable(request.getHeader(tokenHeaderName));

        return authenticationToken.map(s -> s.substring(bearerPrefix.length()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("No token provided."));
    }

    public String generateToken(String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(loggedInExpirationDays));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    public boolean isTokenValidForUser(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    public boolean isValidRequestHeader(HttpServletRequest request) {
        final String requestHeader = request.getHeader(tokenHeaderName);

        return requestHeader != null && requestHeader.startsWith(bearerPrefix);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
