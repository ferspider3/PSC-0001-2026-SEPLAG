package com.projeto.api.controller.v1;

import com.projeto.api.domain.Album;
import com.projeto.api.repository.AlbumRepository;
import com.projeto.api.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate; // IMPORTANTE
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final S3Service s3Service;
    private final SimpMessagingTemplate messagingTemplate; // Injeção para WebSocket

    public AlbumController(AlbumRepository albumRepository, 
                           S3Service s3Service, 
                           SimpMessagingTemplate messagingTemplate) {
        this.albumRepository = albumRepository;
        this.s3Service = s3Service;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<Page<Album>> getAll(
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) String artistType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") 
                    ? Sort.by(sortBy).descending() 
                    : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Album> result;

        if (artistType != null && !artistType.isEmpty()) {
            result = albumRepository.findByArtistType(artistType.toUpperCase(), pageable);
        } else if (artistName != null && !artistName.isEmpty()) {
            result = albumRepository.findByArtistName(artistName, pageable);
        } else {
            result = albumRepository.findAll(pageable);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Album> create(@RequestBody Album album) {
        Album savedAlbum = albumRepository.save(album);
        
        // Requisito (c): Notifica o front-end via WebSocket
        messagingTemplate.convertAndSend("/topic/new-album", savedAlbum);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
    }

    @PostMapping(value = "/{id}/cover", consumes = "multipart/form-data")
    public ResponseEntity<Album> uploadCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return albumRepository.findById(id).map(album -> {
            try {
                String fileName = s3Service.uploadFile(file);
                album.setCoverUrl(fileName);
                Album updatedAlbum = albumRepository.save(album);
                
                // Opcional: Notificar também quando a capa for atualizada
                messagingTemplate.convertAndSend("/topic/new-album", updatedAlbum);
                
                return ResponseEntity.ok(updatedAlbum);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Album>build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}