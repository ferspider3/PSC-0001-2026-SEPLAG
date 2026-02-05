package com.projeto.api.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "albums")
@Data
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String coverUrl;

    // Adicionamos o FetchType.EAGER para garantir que os artistas apareçam no GET do álbum
    @ManyToMany(mappedBy = "albums", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("albums")
    private Set<Artist> artists = new HashSet<>();
}