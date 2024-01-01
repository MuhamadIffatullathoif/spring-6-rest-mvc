package com.iffat.spring6.restmvc.repositories;

import com.iffat.spring6.restmvc.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
