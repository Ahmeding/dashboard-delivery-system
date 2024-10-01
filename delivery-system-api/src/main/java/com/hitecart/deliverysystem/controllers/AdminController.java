package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.controllers.api.AdminApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController implements AdminApi {
    @Override
    public ResponseEntity<String> isAdmin() {
        return ResponseEntity.ok("is admin");
    }
}
