package com.hitecart.deliverysystem.config;

import com.hitecart.deliverysystem.services.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt ;
        final String userEmail ;

        if(!StringUtils.hasLength(authHeader) || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
        return;
        }

        jwt= authHeader.substring(7);
        userEmail= jwtService.extractUsername(jwt);

        if(StringUtils.hasLength(userEmail) &&
                SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt,userDetails)){
                //SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                token.setDetails( new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                /*securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);*/
                SecurityContextHolder.getContext().setAuthentication(token);

            }
        }
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        res.setHeader("Access-Control-Allow-Methods", "GET,POST,PATCH,DELETE,PUT,OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "86400");

        filterChain.doFilter(request,res);
    }
}
