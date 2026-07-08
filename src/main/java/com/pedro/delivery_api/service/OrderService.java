package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.OrderItemRequestDTO;
import com.pedro.delivery_api.dto.OrderItemResponseDTO;
import com.pedro.delivery_api.dto.OrderRequestDTO;
import com.pedro.delivery_api.dto.OrderResponseDTO;
import com.pedro.delivery_api.entity.*;
import com.pedro.delivery_api.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderResponseDTO create(OrderRequestDTO request) {
        Address address = addressRepository.findById(request.addressId()).orElseThrow(() -> new RuntimeException("Endereço não identificado."));
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new RuntimeException("Cliente não identificado."));

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        List<OrderItemResponseDTO> itemResponse = new ArrayList<>();

        for (OrderItemRequestDTO itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId()).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getPrice());

            orderItemRepository.save(orderItem);

            itemResponse.add(new OrderItemResponseDTO(
                    product.getId(),
                    product.getName(),
                    itemRequest.quantity(),
                    product.getPrice()
            ));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalPrice = totalPrice.add(itemTotal);

        }

        savedOrder.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);

        return new OrderResponseDTO(
                savedOrder.getId(),
                savedOrder.getCustomer().getId(),
                savedOrder.getAddress().getId(),
                itemResponse,
                savedOrder.getTotalPrice(),
                savedOrder.getOrderStatus(),
                savedOrder.getCreatedAt());
    }

    public List<OrderResponseDTO> list() {
        List<Order> listOrder = orderRepository.findAll();
        List<OrderResponseDTO> ordersResponse = new ArrayList<>();

        for (Order order : listOrder) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

            List<OrderItemResponseDTO> itemResponse = new ArrayList<>();
            for (OrderItem item : items) {
                itemResponse.add(new OrderItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ));
            }

            ordersResponse.add(new OrderResponseDTO(
                    order.getId(),
                    order.getCustomer().getId(),
                    order.getAddress().getId(),
                    itemResponse,
                    order.getTotalPrice(),
                    order.getOrderStatus(),
                    order.getCreatedAt()
            ));
        }
        return ordersResponse;
    }

    public OrderResponseDTO listById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
        List<OrderItem> items = orderItemRepository.findByOrderId(id);

        List<OrderItemResponseDTO> itemResponse = new ArrayList<>();
        for (OrderItem item : items) {
            itemResponse.add(new OrderItemResponseDTO(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getUnitPrice()
            ));
        }

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getAddress().getId(),
                itemResponse,
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getCreatedAt()
        );
    }

    public OrderResponseDTO update(Long id, OrderResponseDTO request) {

    }
}