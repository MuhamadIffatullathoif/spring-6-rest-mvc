package com.iffat.spring6.restmvc.repositories;

import com.iffat.spring6.restmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, Long> {
}
