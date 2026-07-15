package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.CategoryResponseDTO;
import com.pedro.delivery_api.dto.ProductRequestDTO;
import com.pedro.delivery_api.dto.ProductResponseDTO;
import com.pedro.delivery_api.entity.Category;
import com.pedro.delivery_api.entity.Product;
import com.pedro.delivery_api.exception.DuplicateProductException;
import com.pedro.delivery_api.exception.ResourceNotFoundException;
import com.pedro.delivery_api.repository.CategoryRepository;
import com.pedro.delivery_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public ProductResponseDTO create(ProductRequestDTO request) {
        if(productRepository.findByName(request.name()).isPresent()) {
            throw new DuplicateProductException("Produto já existente");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setAvailable(request.available());
        product.setStockQuantity(request.stockQuantity());

      Category category =  categoryRepository.findById(request.categoryId()).orElseThrow(() ->new ResourceNotFoundException("Categoria não encontrada."));
      product.setCategory(category);
      Product productSaved = productRepository.save(product);

        return new ProductResponseDTO(
                productSaved.getId(),
                productSaved.getName(),
                productSaved.getDescription(),
                productSaved.getPrice(),
                productSaved.getStockQuantity(),
                productSaved.getAvailable(),
                new CategoryResponseDTO(
                        productSaved.getCategory().getId(),
                        productSaved.getCategory().getName(),
                        productSaved.getCategory().getActive()
                )
        );
    }
        public List<ProductResponseDTO> list() {
            List<Product> listProduct = productRepository.findAll();


            return listProduct.stream()
                    .map(c -> new ProductResponseDTO(
                            c.getId(),
                            c.getName(),
                            c.getDescription(),
                            c.getPrice(),
                            c.getStockQuantity(),
                            c.getAvailable(),
                            new CategoryResponseDTO(
                                    c.getCategory().getId(),
                                    c.getCategory().getName(),
                                    c.getCategory().getActive()
                            )
                    ))
                    .toList();
    }

    public ProductResponseDTO listById(Long id) {
        Product product =  productRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Produto não encontrado."));
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getAvailable(),
                new CategoryResponseDTO(
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getActive()
        ));  // category é uma entidade (Category), mas o DTO espera um CategoryResponseDTO
            // por isso preciso converter, montando um novo CategoryResponseDTO com os dados da entidade
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() ->new ResourceNotFoundException("Categoria não encontrada."));
        product.setCategory(category);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setAvailable(request.available());


        Product savedUpdate = productRepository.save(product);

        return new ProductResponseDTO(
                savedUpdate.getId(),
                savedUpdate.getName(),
                savedUpdate.getDescription(),
                savedUpdate.getPrice(),
                savedUpdate.getStockQuantity(),
                savedUpdate.getAvailable(),
                new CategoryResponseDTO(
                savedUpdate.getCategory().getId(),
                savedUpdate.getCategory().getName(),
                savedUpdate.getCategory().getActive()
        ));  // category é uma entidade (Category), mas o DTO espera um CategoryResponseDTO
        // por isso preciso converter, montando um novo CategoryResponseDTO com os dados da entidade
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        productRepository.delete(product);
    }
}
