package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.ClientApi;
import com.hitecart.deliverysystem.dto.ClientDto;
import com.hitecart.deliverysystem.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientController implements ClientApi {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ResponseEntity<ClientDto> save(ClientDto dto) {
        return ResponseEntity.ok(clientService.save(dto));
    }

    @Override
    public ResponseEntity<ClientDto> findById(Integer id) {
        return  ResponseEntity.ok(clientService.findById(id));
    }

    @Override
    public ResponseEntity<ClientDto> findByEmail(String email) {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    @Override
    public ResponseEntity<List<ClientDto>> findAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @Override
    public ResponseEntity<ClientDto> updateIsActive(Integer id, Boolean isActive) {
        return ResponseEntity.ok(clientService.updateIsActive(id,isActive));
    }

    @Override
    public void delete(Integer id){
        clientService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<ClientDto>> findAllClients(int page, int size) {
        return ResponseEntity.ok(clientService.findAllClients(page,size));
    }
}
