package com.accenture.repository;

import com.accenture.repository.entity.Velo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeloDao extends JpaRepository<Velo, Long> {
}
