package com.wcs.employee_management.employee_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.security.KeyRep.Type.SECRET;

@Component
public class JWTUtil {

    private static final String SECRET = "my_super_secure_256_bit_secret_key!123";
    private final static long expiration = 3600000;
    private static final String JWT_ROLES_KEY = "roles";

    public static String generateToken(String email, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String rolesString = roles.stream().collect(Collectors.joining(","));
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", rolesString)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)  // ✅ use correct method
                .compact();
    }

    public String extractEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return getAllClaimsFromToken(token).get(JWT_ROLES_KEY, List.class);
    }

    public boolean isTokenValid(String token) {
        var claims = getAllClaimsFromToken(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}




