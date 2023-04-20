package org.example.jwt;


import io.jsonwebtoken.Claims;
import org.example.service.security.UserDetailsCustom;

import java.security.Key;

public interface JwtService {

    Claims extractClaims(String token);

    Key getKey();

    String generateToken(UserDetailsCustom userDetailsCustom);

    boolean isValidToken(String token);
}
