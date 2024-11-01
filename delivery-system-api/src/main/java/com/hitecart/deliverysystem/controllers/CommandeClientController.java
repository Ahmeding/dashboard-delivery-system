package com.hitecart.deliverysystem.controllers;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.CommandeClientApi;
import com.hitecart.deliverysystem.dto.CommandeClientDto;
import com.hitecart.deliverysystem.dto.CommandeClientResponse;
import com.hitecart.deliverysystem.services.CommandeClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandeClientController implements CommandeClientApi {

    private final  CommandeClientService commandeClientService;



    @Override
    public ResponseEntity<CommandeClientDto> save(CommandeClientDto dto) {
        return ResponseEntity.ok(commandeClientService.save(dto));
    }

    @Override
    public ResponseEntity<CommandeClientResponse> findById(Integer id) {
        return ResponseEntity.ok(commandeClientService.findById(id));
    }

    @Override
    public ResponseEntity<CommandeClientResponse> findByCode(String code) {
        return ResponseEntity.ok(commandeClientService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<CommandeClientResponse>> findAll() {
        return ResponseEntity.ok(commandeClientService.findAll());
    }

    @Override
    public void delete(Integer id) {
        commandeClientService.delete(id);
    }

    @Override
    public ResponseEntity<PageResponse<CommandeClientResponse>> findAllCommandeClients(int page, int size) {
        return ResponseEntity.ok(commandeClientService.findAllCmdClient(page,size));
    }
}
