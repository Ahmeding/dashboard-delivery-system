package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.UserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    @Override
    public ResponseEntity<String> isUser() {
        return ResponseEntity.ok("is user");
    }
}
