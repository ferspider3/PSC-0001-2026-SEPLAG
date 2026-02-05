package com.projeto.api.service;

import com.projeto.api.domain.Regional;
import com.projeto.api.dto.RegionalResponseDTO;
import com.projeto.api.repository.RegionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegionalSyncService {

    private final RegionalRepository repository;
    private final String API_URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    public RegionalSyncService(RegionalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void syncRegionais() { 
        RestTemplate restTemplate = new RestTemplate();
        RegionalResponseDTO[] response = restTemplate.getForObject(API_URL, RegionalResponseDTO[].class);
        
        if (response == null) return;
        List<RegionalResponseDTO> apiData = Arrays.asList(response);

        Map<Integer, Regional> localAtivas = repository.findByAtivoTrue().stream()
                .collect(Collectors.toMap(Regional::getId, r -> r));

        for (RegionalResponseDTO dto : apiData) {
            Regional existente = localAtivas.get(dto.getId());

            if (existente == null) {
                saveNew(dto);
            } else if (!existente.getNome().equals(dto.getNome())) {
                existente.setAtivo(false);
                repository.save(existente);
                saveNew(dto);
            }
            localAtivas.remove(dto.getId());
        }

        localAtivas.values().forEach(r -> {
            r.setAtivo(false);
            repository.save(r);
        });
    }

    private void saveNew(RegionalResponseDTO dto) {
        Regional novo = new Regional();
        novo.setId(dto.getId());
        novo.setNome(dto.getNome());
        novo.setAtivo(true);
        repository.save(novo);
    }
}