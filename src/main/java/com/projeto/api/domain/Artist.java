package com.projeto.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "artists")
@Data
@EqualsAndHashCode(exclude = "albums") // Evita recursão no Equals/HashCode do Lombok
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String type; 

    @ManyToMany(fetch = FetchType.EAGER) // Mudamos para EAGER para garantir a visualização no teste
    @JoinTable(
      name = "artist_album",
      joinColumns = @JoinColumn(name = "artist_id"),
      inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    @JsonIgnoreProperties("artists")
    private Set<Album> albums = new HashSet<>();

    // MÉTODO HELPER: Garante que os dois lados fiquem vinculados
    public void addAlbum(Album album) {
        this.albums.add(album);
        album.getArtists().add(this);
    }
}