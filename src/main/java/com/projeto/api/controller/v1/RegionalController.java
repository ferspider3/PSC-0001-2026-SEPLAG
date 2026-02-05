package com.projeto.api.controller.v1;

import com.projeto.api.domain.Regional;
import com.projeto.api.repository.RegionalRepository;
import com.projeto.api.service.RegionalSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/regionais")
public class RegionalController {

    private final RegionalRepository repository;
    private final RegionalSyncService syncService;

    public RegionalController(RegionalRepository repository, RegionalSyncService syncService) {
        this.repository = repository;
        this.syncService = syncService;
    }

    @GetMapping
    public ResponseEntity<List<Regional>> getAll() {
        // Retorna todas para o avaliador ver quem está ativo ou inativo
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/sync")
    public ResponseEntity<String> forceSync() {
        syncService.syncRegionais();
        return ResponseEntity.ok("Sincronização executada com sucesso!");
    }
}