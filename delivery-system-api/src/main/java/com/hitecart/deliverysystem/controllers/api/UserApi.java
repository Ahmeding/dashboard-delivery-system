package com.hitecart.deliverysystem.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/user")
public interface UserApi {

    @GetMapping("/isuser")
    ResponseEntity<String> isUser();
}
