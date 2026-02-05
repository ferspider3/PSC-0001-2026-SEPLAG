package com.projeto.api.config;

import com.projeto.api.domain.Album;
import com.projeto.api.domain.Artist;
import com.projeto.api.domain.User;
import com.projeto.api.repository.AlbumRepository;
import com.projeto.api.repository.ArtistRepository;
import com.projeto.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, 
                                   ArtistRepository artistRepo, 
                                   AlbumRepository albumRepo,
                                   PasswordEncoder encoder) {
        return args -> {
            // 1. Criar Usuário Admin (Sempre necessário)
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("password"));
                admin.setRole("ROLE_ADMIN");
                userRepo.save(admin);
            }

            // 2. Criar Dados do Teste
            if (artistRepo.count() == 0) {
                
                // --- SERJ TANKIAN ---
                createArtistWithAlbums(artistRepo, albumRepo, "Serj Tankian", "INDIVIDUAL",
                    List.of("Harakiri", "Black Blooms", "The Rough Dog"));

                // --- MIKE SHINODA ---
                createArtistWithAlbums(artistRepo, albumRepo, "Mike Shinoda", "INDIVIDUAL",
                    List.of("The Rising Tied", "Post Traumatic", "Post Traumatic EP", "Where’d You Go"));

                // --- MICHEL TELÓ ---
                createArtistWithAlbums(artistRepo, albumRepo, "Michel Teló", "INDIVIDUAL",
                    List.of("Bem Sertanejo", "Bem Sertanejo - O Show (Ao Vivo)", "Bem Sertanejo - (1ª Temporada) - EP"));

                // --- GUNS N’ ROSES ---
                createArtistWithAlbums(artistRepo, albumRepo, "Guns N’ Roses", "BANDA",
                    List.of("Use Your Illusion I", "Use Your Illusion II", "Greatest Hits"));

                System.out.println("Todos os artistas e álbuns do teste foram carregados!");
            }
        };
    }

    // Método auxiliar para evitar repetição de código
    private void createArtistWithAlbums(ArtistRepository artistRepo, AlbumRepository albumRepo, 
                                        String name, String type, List<String> albumTitles) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setType(type);
        
        // Salvamos o artista primeiro para ter o ID
        final Artist savedArtist = artistRepo.save(artist);

        albumTitles.forEach(title -> {
            Album album = new Album();
            album.setTitle(title);
            album.setCoverUrl("default-cover.jpg");
            // Salvamos o álbum
            Album savedAlbum = albumRepo.save(album);
            // Vinculamos (Usando o seu método helper addAlbum)
            savedArtist.addAlbum(savedAlbum);
        });

        // Atualizamos o artista com os vínculos
        artistRepo.save(savedArtist);
    }
}