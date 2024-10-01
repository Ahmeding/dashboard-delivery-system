package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.*;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.exception.RequiredFieldException;
import com.hitecart.deliverysystem.model.Utilisateur;
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.validator.ChauffeurValidator;
import com.hitecart.deliverysystem.validator.LoginValidator;
import com.hitecart.deliverysystem.validator.SignupRequestValidator;
import com.hitecart.deliverysystem.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService{

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthentificationServiceImpl(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private void checkDto(SignupRequest signupRequest, String logMsg,String exceptionMsg ) {
        List<String> errors = SignupRequestValidator.validate(signupRequest);
        if (!errors.isEmpty()) {
            log.error(logMsg, signupRequest);
            throw new InvalidEntityException(exceptionMsg, ErrorCodes.UTILISATEUR_NOT_VALID, errors);

        }
    }
    @Override
    public UtilisateurDto signup(SignupRequest signupRequest) {
        checkDto(signupRequest,"Utilisateur n'est valid","l'utilisateur n'est pas valid");

        var user = utilisateurRepository.findByEmail(signupRequest.getEmail());
//                .orElseThrow(() -> new IllegalArgumentException("Adresse email ou mot de passe invalide"));
//
        List<String> errors = new ArrayList<>();

        if(user.isEmpty()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(signupRequest.getLastName());
            utilisateur.setPrenom(signupRequest.getFirstName());
            utilisateur.setEmail(signupRequest.getEmail());
            utilisateur.setMotDePasse(passwordEncoder.encode(signupRequest.getPassword()));
            utilisateur.setRole(signupRequest.getRole());
            return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
        }else {
            errors.add("L'adresse mail est deja utilisée");
            System.out.println(errors.get(0));
            throw new InvalidOperationException("L'adresse mail... est deja utilisée",ErrorCodes.UTILISATEUR_NOT_VALID_OPERATION,errors);
        }
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        List<String> errors =LoginValidator.validate(signinRequest);
        log.error("------------- LoginValidator.validate(signinRequest) -----------------"  + errors.size());
        if(!errors.isEmpty()) {
            throw new RequiredFieldException("Veuillez rensiegner les le login et le mode passe!",ErrorCodes.BAD_CREDENTIALS, errors);
        }

        var user = utilisateurRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Adresse email ou mot de passe invalide"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(),
                        signinRequest.getPassword()
                )
        );

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        // TODO in case the role is empty gives error!!
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String userEmail= jwtService.extractUsername(refreshTokenRequest.getToken());
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),utilisateur)){
            var jwt= jwtService.generateToken(utilisateur);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }

        return null;
    }
}
