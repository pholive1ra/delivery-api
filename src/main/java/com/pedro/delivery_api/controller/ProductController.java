package com.pedro.delivery_api.controller;

import com.pedro.delivery_api.dto.ProductRequestDTO;
import com.pedro.delivery_api.dto.ProductResponseDTO;
import com.pedro.delivery_api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ProductResponseDTO create(@RequestBody ProductRequestDTO request) {
        return productService.create(request);
    }

    @GetMapping("")
    public List<ProductResponseDTO> list() {
        return productService.list();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO listById(@PathVariable Long id) {
        return productService.listById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @RequestBody ProductRequestDTO request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
