package com.demo.storeweb.service;

import com.demo.storeweb.dto.ProductDTO;
import com.demo.storeweb.model.Product;
import com.demo.storeweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).get();
    }

    public Product createProduct(ProductDTO productDTO) {
        // Luu anh vao folder public/images/
        MultipartFile image = productDTO.getImageFile();
        String storageFileName = image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try(InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // get các thuộc tính nhận vào từ DTO sau đó set cho product để lưu vào db
        Product product = Product.builder()
                .name(productDTO.getName())
                .brand(productDTO.getBrand())
                .category(productDTO.getCategory())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .imageFileName(storageFileName)
                .build();

        return productRepository.save(product);
    }

    public ProductDTO showDataInEditPage(int id) {
        Product product = getProductById(id);

        ProductDTO productDTO = ProductDTO.builder()
                .name(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();

        return productDTO;
    }

    public Product updateProduct(int id, ProductDTO productDTO) {
        Product product = getProductById(id);

        if(!productDTO.getImageFile().isEmpty()) {

            String uploadDir = "public/images/";

            // save new image file
            MultipartFile image = productDTO.getImageFile();
            String storageFileName = image.getOriginalFilename();

            product.setImageFileName(storageFileName);
        }

        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());

        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}
