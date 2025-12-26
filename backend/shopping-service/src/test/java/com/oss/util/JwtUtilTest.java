package com.oss.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"; // Matching
                                                                                                   // application.properties
    private long expiration = 86400000;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", expiration);
    }

    @Test
    public void testGenerateAndValidateToken() {
        UserDetails userDetails = new User("test@example.com", "password", new ArrayList<>());

        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtUtil.extractUsername(token);
        assertEquals("test@example.com", username);

        boolean isValid = jwtUtil.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }
}
