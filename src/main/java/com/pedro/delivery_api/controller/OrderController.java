package com.pedro.delivery_api.controller;


import com.pedro.delivery_api.dto.OrderRequestDTO;
import com.pedro.delivery_api.dto.OrderResponseDTO;
import com.pedro.delivery_api.dto.OrderStatusUpdateDTO;
import com.pedro.delivery_api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public OrderResponseDTO create (@RequestBody OrderRequestDTO request) {
        return orderService.create(request);
    }

    @GetMapping("")
    public List<OrderResponseDTO> list() {
        return orderService.list();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO listById(@PathVariable Long id) {
        return orderService.listById(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDTO update(@PathVariable Long id, @RequestBody OrderStatusUpdateDTO request) {
        return orderService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
