package com.projeto.api.repository;

import com.projeto.api.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT DISTINCT a FROM Album a JOIN a.artists art WHERE " +
           "(:artistName IS NULL OR LOWER(art.name) LIKE LOWER(CONCAT('%', :artistName, '%')))")
    Page<Album> findByArtistName(@Param("artistName") String artistName, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Album a JOIN a.artists art WHERE art.type = :type")
    Page<Album> findByArtistType(@Param("type") String type, Pageable pageable);
}