package com.accenture.repository;

import com.accenture.repository.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDao extends JpaRepository<Location, Integer> {
}
