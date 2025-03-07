package com.hitecart.deliverysystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.TokenExpiredException;
import com.hitecart.deliverysystem.handlers.ErrorDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;

public class JwtErrorHelper implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Throwable cause = authException.getCause();
        ErrorDto errorDto;

        if(cause instanceof ExpiredJwtException) {
           // throw new TokenExpiredException("Expired JWT token", ErrorCodes.TOKEN_EXPIRED);
            errorDto=ErrorDto.builder()
                    .errorCodes(ErrorCodes.TOKEN_EXPIRED)
                    .msg("Token has expired")
                    .httpCode(HttpStatus.UNAUTHORIZED.value())
                    .errors(Collections.singletonList("Token has expired, please login again!"))
                    .build();

         }else{
            System.out.println("BAD JWT token");
            errorDto=ErrorDto.builder()
                    .errorCodes(ErrorCodes.BAD_TOKEN)
                    .msg("Bad Token")
                    .httpCode(HttpStatus.UNAUTHORIZED.value())
                    .errors(Collections.singletonList("Invalid JWT token!"))
                    .build();

        }
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorDto));
    }
}
