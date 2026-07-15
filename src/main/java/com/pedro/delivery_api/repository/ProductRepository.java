package com.pedro.delivery_api.repository;

import com.pedro.delivery_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List <Product> findByCategoryId(Long categoryId);
    List <Product> findByAvailable(Boolean available);
    List <Product> findByNameContaining(String name);
    Optional<Product> findByName(String name);
}
