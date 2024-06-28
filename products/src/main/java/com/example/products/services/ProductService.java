package com.example.products.services;

import com.example.products.model.Product;
import com.example.products.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Obtener todos los productos
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // Crear un nuevo producto
    public ResponseEntity<Object> newProduct(Product product) {
        productRepository.save(product);
        return new ResponseEntity<>("Producto creado exitosamente", HttpStatus.CREATED);
    }

    // Eliminar un producto por ID
    public ResponseEntity<Object> deleteProduct(Long id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Producto eliminado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un producto existente
    public ResponseEntity<Object> updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setSku(updatedProduct.getSku());
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStatus(updatedProduct.getStatus());

            productRepository.save(existingProduct);

            return new ResponseEntity<>("Producto actualizado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // Buscar un producto por ID
    public ResponseEntity<Object> findByIdProduct(Long id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            return new ResponseEntity<>(existingProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
