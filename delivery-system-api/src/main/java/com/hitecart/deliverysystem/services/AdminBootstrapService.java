package com.hitecart.deliverysystem.services;


public interface AdminBootstrapService {

   public void createFirstAdmin(
            String email,
            String firstname,
            String lastname,
            char[] rawPassword
    );
}
