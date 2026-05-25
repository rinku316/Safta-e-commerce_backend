package com.kodnest.App.ecommerce.Services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kodnest.App.ecommerce.Entitys.JWTToken;
import com.kodnest.App.ecommerce.Entitys.User;
import com.kodnest.App.ecommerce.Repositories.JWTTokenRepository;
import com.kodnest.App.ecommerce.Repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    private final Key SIGNING_KEY;
    private final UserRepository userRepository;
    private final JWTTokenRepository jwtTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final long jwtExpiration;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            JWTTokenRepository jwtTokenRepository,
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {

        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtExpiration = jwtExpiration;

        if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException(
                    "JWT_SECRET must be at least 64 bytes for HS512."
            );
        }

        this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public User authenticate(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    public String generateToken(User user) {

        String token;
        LocalDateTime now = LocalDateTime.now();

        JWTToken existingToken = jwtTokenRepository.findByUserId(user.getUserId());

        if (existingToken != null && now.isBefore(existingToken.getExpiresAt())) {
            token = existingToken.getToken();
        } else {

            token = generateNewToken(user);

            if (existingToken != null) {
                jwtTokenRepository.delete(existingToken);
            }

            saveToken(user, token);
        }

        return token;
    }

    private String generateNewToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public void saveToken(User user, String token) {

        JWTToken jwtToken = new JWTToken(
                user,
                token,
                LocalDateTime.now().plusHours(1)
        );

        jwtTokenRepository.save(jwtToken);
    }

    public void logout(User user) {

        jwtTokenRepository.deleteByUserId(user.getUserId());
    }

    public boolean validateToken(String token) {

        try {

            System.out.println("VALIDATING TOKEN...");

            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);

            if (jwtToken.isPresent()) {

                System.out.println("Token Expiry: " + jwtToken.get().getExpiresAt());
                System.out.println("Current Time: " + LocalDateTime.now());

                return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
            }

            return false;

        } catch (Exception e) {

            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}