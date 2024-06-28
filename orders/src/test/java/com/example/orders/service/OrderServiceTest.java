package com.example.orders.service;

import com.example.orders.model.Order;
import com.example.orders.repositories.OrderRepository;
import com.example.orders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test "Obtener Órdenes"
    @Test
    public void testGetOrders() {
        Order order = new Order(1L, 1L, 49.99, 2L, 99.98, LocalDate.now(), "Notas de prueba");
        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.getOrders();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProductId());
    }

    // Test "Eliminar Orden"
    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        ResponseEntity<Object> response = orderService.deleteOrder(orderId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Orden eliminada exitosamente", response.getBody());
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    // Test "Crear Orden"
    @Test
    public void testNewOrder() {
        Order order = new Order();
        order.setProductId(1L);
        order.setUnitPrice(49.99);
        order.setQuantity(2L);
        order.setTotal(99.98);
        order.setDate(LocalDate.now());
        order.setNotes("Notas de prueba");

        String url = "http://localhost:8083/api/products/find/" + order.getProductId();
        when(restTemplate.getForEntity(url, Object.class)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = orderService.newOrder(order);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Orden creada exitosamente", response.getBody());

        verify(orderRepository, times(1)).save(order);
    }

    // Test "Actualizar Orden"
    @Test
    public void testUpdateOrder() {
        Long id = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setProductId(1L);
        existingOrder.setUnitPrice(49.99);
        existingOrder.setQuantity(2L);
        existingOrder.setTotal(99.98);
        existingOrder.setDate(LocalDate.now());
        existingOrder.setNotes("Notas de prueba");

        Order updatedOrder = new Order();
        updatedOrder.setId(id);
        updatedOrder.setProductId(2L);
        updatedOrder.setUnitPrice(59.99);
        updatedOrder.setQuantity(3L);
        updatedOrder.setTotal(179.97);
        updatedOrder.setDate(LocalDate.now());
        updatedOrder.setNotes("Notas actualizadas");

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        ResponseEntity<Object> response = orderService.updateOrder(id, updatedOrder);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Orden actualizada exitosamente", response.getBody());

        verify(orderRepository, times(1)).save(updatedOrder);
    }
}
