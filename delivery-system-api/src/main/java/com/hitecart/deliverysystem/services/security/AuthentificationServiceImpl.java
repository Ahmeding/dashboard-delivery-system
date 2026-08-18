package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.dto.JwtAuthenticationResponse;
import com.hitecart.deliverysystem.dto.RefreshTokenRequest;
import com.hitecart.deliverysystem.dto.SigninRequest;
import com.hitecart.deliverysystem.dto.SignupRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.UserMapper;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dao.TokenRepository;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.validator.SignupRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse signup(SignupRequest request) {
        var userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(()->new IllegalStateException("ROLE USER was not initialized"));
        checkDto(request,
                "User n'est valid",
                "l'utilisateur n'est pas valid");

            Integer createdBy=-1;
        if(userRepository.findAll().isEmpty()){
            createdBy=0;
        }


        var userToRegister = User.builder()
                .lastname(request.getLastName())
                .firstname(request.getFirstName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .roles(List.of(userRole))
                .createdBy(createdBy)
                .build();
        return userMapper.toResponse(userRepository.save(userToRegister));
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {

        var existingUser = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if(!existingUser.isEnabled()){
            throw new InvalidOperationException("An error occurred: your account is currently disabled. " +
                    "Please contact support for assistance.");
        }


        if(existingUser.isAccountLocked()){
            throw new InvalidOperationException("An error occurred: your account is currently Locked. " +
                    "Please contact support for assistance.");
        }
        var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  signinRequest.getEmail(),
                  signinRequest.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user=(User) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        var jwt = jwtService.generateToken(claims,user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        //jwtAuthenticationResponse.setUser(userMapper.toResponse(user));
        jwtAuthenticationResponse.setAccess_token(jwt);
        jwtAuthenticationResponse.setRefresh_token(refreshToken);


        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String userEmail= jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        var claims = new HashMap<String, Object>();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            claims.put("fullName", user.fullName());
            var jwt= jwtService.generateToken(claims, user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setAccess_token(jwt);
            jwtAuthenticationResponse.setRefresh_token(refreshTokenRequest.getToken());
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
            throw new InvalidEntityException(exceptionMsg, ErrorCodes.USER_NOT_VALID, errors);

        }
    }
}
