package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.basic.*;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dao.TokenRepository;
import com.hitecart.deliverysystem.dao.UtilisateurRepository;
import com.hitecart.deliverysystem.validator.SignupRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService{

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    @Override
    public UtilisateurDto signup(SignupRequest request) {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()->new IllegalStateException("ROLE USER was not initialized"));
        checkDto(request,
                "Utilisateur n'est valid",
                "l'utilisateur n'est pas valid");

            Integer createdBy=-1;
        if(utilisateurRepository.findAll().isEmpty()){
            createdBy=0;
        }


        var userToRegister = Utilisateur.builder()
                .lastname(request.getLastName())
                .firstname(request.getFirstName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .roles(List.of(userRole))
                .createdBy(createdBy)
                .build();
        return UtilisateurDto.fromEntity(utilisateurRepository.save(userToRegister));
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        // List<String> errors =LoginValidator.validate(signinRequest);
        /* if(!errors.isEmpty()) {
            throw new RequiredFieldException("Veuillez rensiegner les le login et le mode passe!",ErrorCodes.BAD_CREDENTIALS, errors);
        }*/
        /*
        var user = utilisateurRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Adresse email ou mot de passe invalide"));

*/
        var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  signinRequest.getEmail(),
                  signinRequest.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user=(Utilisateur) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        var jwt = jwtService.generateToken(claims,user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        jwtAuthenticationResponse.setUser(UtilisateurDto.fromEntity(user));
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);


        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String userEmail= jwtService.extractUsername(refreshTokenRequest.getToken());
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userEmail).orElseThrow();
        var claims = new HashMap<String, Object>();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),utilisateur)){
            claims.put("fullName", utilisateur.fullName());
            var jwt= jwtService.generateToken(claims,utilisateur);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

/*    @Transactional
    public void activateAcount(String token) {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt()));{
            //sendValidationEmail(savedToken.getUser);
            System.out.println("ok expired");
            throw new RuntimeException("Activation Token has expired, A new token has been sent");
        }

        var user = utilisateurRepository.findById(savedToken.getUser().getId())
                .orElseThrow( ()-> new UsernameNotFoundException("Invalid user") );

        user.setEnabled(true);
        utilisateurRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);


    }*/

    private void checkDto(SignupRequest signupRequest, String logMsg,String exceptionMsg ) {
        List<String> errors = SignupRequestValidator.validate(signupRequest);
        if (!errors.isEmpty()) {
            log.error(logMsg, signupRequest);
            throw new InvalidEntityException(exceptionMsg, ErrorCodes.UTILISATEUR_NOT_VALID, errors);

        }
    }
}
