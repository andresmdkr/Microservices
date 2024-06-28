package com.example.orders.services;

import com.example.orders.model.Order;
import com.example.orders.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderRepository orderRepository;

    // Obtener todas las órdenes
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    // Crear una nueva orden
    public ResponseEntity<Object> newOrder(Order order) {
        Long productId = order.getProductId();
        String url = "http://localhost:8083/api/products/find/" + productId;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Producto encontrado, guardar la orden
                orderRepository.save(order);
                return new ResponseEntity<>("Orden creada exitosamente", HttpStatus.CREATED);
            } else {
                // Producto no encontrado, no guardar la orden
                return new ResponseEntity<>("Producto no encontrado, orden no creada", HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return new ResponseEntity<>("Producto no encontrado, orden no creada", HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una orden por ID
    public ResponseEntity<Object> deleteOrder(Long id) {
        Optional<Order> existingOrderOptional = orderRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            orderRepository.deleteById(id);
            return new ResponseEntity<>("Orden eliminada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una orden existente
    public ResponseEntity<Object> updateOrder(Long id, Order updatedOrder) {
        Optional<Order> existingOrderOptional = orderRepository.findById(id);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            existingOrder.setProductId(updatedOrder.getProductId());
            existingOrder.setUnitPrice(updatedOrder.getUnitPrice());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setTotal(updatedOrder.getTotal());
            existingOrder.setDate(updatedOrder.getDate());
            existingOrder.setNotes(updatedOrder.getNotes());

            orderRepository.save(existingOrder);

            return new ResponseEntity<>("Orden actualizada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
