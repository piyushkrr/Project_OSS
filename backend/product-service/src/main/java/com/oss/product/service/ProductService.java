package com.oss.product.service;

import com.oss.product.dto.ProductDTO;
import com.oss.product.entity.Image;
import com.oss.product.entity.Product;
import com.oss.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public Product createProduct(ProductDTO request, List<MultipartFile> files) throws IOException {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .mainImageUrl(request.getMainImageUrl())
                .images(new ArrayList<>())
                .build();

        if (files != null && !files.isEmpty()) {
            MultipartFile firstFile = files.get(0);
            product.setMainImage(firstFile.getBytes());
            product.setImageType(firstFile.getContentType());

            for (MultipartFile file : files) {
                Image image = Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(file.getBytes())
                        .build();
                product.getImages().add(image);
            }
        }
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product updateProduct(Long id, ProductDTO request, List<MultipartFile> files) throws IOException {
        Product product = getProductById(id);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());

        if (request.getMainImageUrl() != null) {
            product.setMainImageUrl(request.getMainImageUrl());
        }

        if (files != null && !files.isEmpty()) {
            MultipartFile firstFile = files.get(0);
            product.setMainImage(firstFile.getBytes());
            product.setImageType(firstFile.getContentType());

            // In a real app, you might want to clear old images or intelligently update
            // them
            // For now, let's just add the new ones
            for (MultipartFile file : files) {
                Image image = Image.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(file.getBytes())
                        .build();
                product.getImages().add(image);
            }
        }
        return repository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        repository.delete(product);
    }
}
