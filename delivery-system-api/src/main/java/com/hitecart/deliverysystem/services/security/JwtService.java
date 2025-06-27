package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(Map<String,Object> claims,UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
    String generateRefreshToken(Map<String,Object> extraClaims, User user);
}
