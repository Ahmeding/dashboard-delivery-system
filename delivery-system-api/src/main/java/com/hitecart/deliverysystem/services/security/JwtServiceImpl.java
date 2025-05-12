package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
     private String SECURE_256BIT_BASE64_SECRET;


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, Utilisateur user) {

        // Add roles to refresh token if needed
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        extraClaims.put("roles", roles);

         return Jwts.builder().setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 684800000))
                .signWith(getSiginKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateToken(Map<String, Object> claims,UserDetails userDetails) {
        // add roles to claims automatically
        if(userDetails instanceof Utilisateur) {
            Utilisateur utilisateur = (Utilisateur) userDetails;
            List<String> roles =  utilisateur.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();
            claims.put("roles", roles);
            claims.put("username", utilisateur.getEmail());
            claims.put("userId", utilisateur.getId());
        }

         return createToken(claims, userDetails);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSiginKey())
                .parseClaimsJws(token).getBody();
    }





    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) /* ms    m    h*/
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSiginKey(),SignatureAlgorithm.HS256)
                .compact();
    }



    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    private Key getSiginKey() {
         log.error(SECURE_256BIT_BASE64_SECRET);
        byte[] keyBytes= Decoders.BASE64.decode(SECURE_256BIT_BASE64_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String extractIdEntreprise(String token) {
//        final Claims claims = extractAllClaims(token);
//        return claims.get("idEntreprise", String.class);
//    }
}
