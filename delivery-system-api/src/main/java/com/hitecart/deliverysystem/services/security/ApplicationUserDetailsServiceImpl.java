package com.hitecart.deliverysystem.services.security;

import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.repository.UtilisateurRepository;
import com.hitecart.deliverysystem.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApplicationUserDetailsServiceImpl implements ApplicationUserDetailsService{

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String useremail)
                    throws UsernameNotFoundException {

                return utilisateurRepository.findByEmail(useremail)
                        .orElseThrow(
                                () -> new UsernameNotFoundException(
                                        "Utilisateur n'est pas trouvé"
                                )
                        );
               }
        };
    }

}
