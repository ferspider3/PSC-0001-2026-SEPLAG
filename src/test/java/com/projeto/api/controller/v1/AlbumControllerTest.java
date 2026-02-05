package com.projeto.api.controller.v1;

import com.projeto.api.domain.Album;
import com.projeto.api.repository.AlbumRepository;
import com.projeto.api.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    @DisplayName("Deve retornar lista de álbuns paginada com sucesso")
    @WithMockUser
    void shouldReturnPaginatedAlbums() throws Exception {
        // Cenário (Given)
        Album album = new Album();
        album.setId(1L);
        album.setTitle("Hybrid Theory");
        
        PageImpl<Album> page = new PageImpl<>(List.of(album));
        
        when(albumRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/v1/albums")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Hybrid Theory"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Deve filtrar álbuns por tipo de artista")
    @WithMockUser
    void shouldFilterAlbumsByArtistType() throws Exception {
        Album album = new Album();
        album.setTitle("Harakiri");
        PageImpl<Album> page = new PageImpl<>(List.of(album));

        when(albumRepository.findByArtistType(eq("INDIVIDUAL"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/v1/albums")
                .param("artistType", "INDIVIDUAL")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Harakiri"));
    }
}