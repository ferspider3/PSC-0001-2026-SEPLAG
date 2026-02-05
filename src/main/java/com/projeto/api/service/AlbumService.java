package com.projeto.api.service;

import com.projeto.api.domain.Album;
import com.projeto.api.domain.Artist;
import com.projeto.api.dto.AlbumRequestDTO;
import com.projeto.api.repository.AlbumRepository;
import com.projeto.api.repository.ArtistRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.HashSet;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final MinioService minioService;
    private final SimpMessagingTemplate messagingTemplate;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, 
                        MinioService minioService, SimpMessagingTemplate messagingTemplate) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.minioService = minioService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Album save(AlbumRequestDTO dto, List<MultipartFile> covers) {
        Album album = new Album();
        album.setTitle(dto.title());
        
        List<Artist> artists = artistRepository.findAllById(dto.artistIds());
        album.setArtists(new HashSet<>(artists));

        Album savedAlbum = albumRepository.save(album);

        if (covers != null) {
            covers.forEach(file -> minioService.upload(file, "album-" + savedAlbum.getId()));
        }

        messagingTemplate.convertAndSend("/topic/new-album", "Novo álbum cadastrado: " + savedAlbum.getTitle());

        return savedAlbum;
    }
}