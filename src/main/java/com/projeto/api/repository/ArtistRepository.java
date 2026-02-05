package com.projeto.api.repository;

import com.projeto.api.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
    List<Artist> findByType(String type);
    
    List<Artist> findByNameContainingIgnoreCase(String name);
}