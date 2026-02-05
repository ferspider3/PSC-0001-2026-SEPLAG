package com.projeto.api.repository;

import com.projeto.api.domain.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegionalRepository extends JpaRepository<Regional, Long> {
    List<Regional> findByAtivoTrue();
}