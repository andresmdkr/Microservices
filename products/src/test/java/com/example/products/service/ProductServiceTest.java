package com.example.products.service;


import com.example.products.model.Product;
import com.example.products.repositories.ProductRepository;
import com.example.products.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp (){
        MockitoAnnotations.openMocks(this);
    }


    //Test "Obtener Productos"
    @Test
    public  void testGetProducts() {
        Product product = new Product(12L, "1234", "Description1", 100.0, true);
        List<Product> products = Collections.singletonList(product);
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getProducts();
        assertEquals(1, result.size());
        assertEquals("1234", result.get(0).getSku());
    }


    //Test "Eliminar Producto"
    @Test
    public void testDeleteProduct(){
        Long productId = 12L;
        Product product = new Product();
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        ResponseEntity<Object> response = productService.deleteProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto eliminado exitosamente", response.getBody());
        verify(productRepository, times(1)).deleteById(productId);
    }


    //Test "Crear Producto"
    @Test
    public void testNewProduct() {
        Product product = new Product();
        product.setSku("1234");
        product.setName("TEST");
        product.setPrice(100.0);
        product.setStatus(true);

        ResponseEntity<Object> response = productService.newProduct(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Producto creado exitosamente", response.getBody());

        verify(productRepository, times(1)).save(product);
    }


    //Test "Actualizar Producto"
    @Test
    public void testUpdateProduct() {

        Long id = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setSku("1234");
        existingProduct.setName("Existing Product");
        existingProduct.setPrice(10.0);
        existingProduct.setStatus(true); // Cambiar a tipo Boolean

        // Creacion de producto ejemplo actualizado
        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setSku("9876");
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(20.0);
        updatedProduct.setStatus(false); // Cambiar a tipo Boolean

        // Simular el comportamiento del repositorio
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Llamar al método a probar
        ResponseEntity<Object> response = productService.updateProduct(id, updatedProduct);

        // Verificar el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto actualizado exitosamente", response.getBody());

        // Verificar si el método save fue llamado con el producto actualizado
        verify(productRepository, times(1)).save(existingProduct);

        // Verificar los atributos del producto actualizado
        assertEquals("9876", existingProduct.getSku());
        assertEquals("Updated Product", existingProduct.getName());
        assertEquals(20.0, existingProduct.getPrice());
        assertEquals(false, existingProduct.getStatus());
    }

}
