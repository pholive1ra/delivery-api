package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.*;
import com.pedro.delivery_api.entity.*;
import com.pedro.delivery_api.exception.InvalidOrderException;
import com.pedro.delivery_api.exception.InvalidOrderStatusException;
import com.pedro.delivery_api.exception.ProductUnavailableException;
import com.pedro.delivery_api.exception.ResourceNotFoundException;
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
        Address address = addressRepository.findById(request.addressId()).orElseThrow(() ->new ResourceNotFoundException("Endereço não identificado."));
        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new ResourceNotFoundException("Cliente não identificado."));

        if(request.items().isEmpty()) {
            throw new InvalidOrderException("Items vazios para criação de pedido.");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

            if (!product.getAvailable()) {
                throw new ProductUnavailableException("Produto indisponível: " + product.getName());
            }

            if (itemRequest.quantity() > product.getStockQuantity()) {
                throw new ProductUnavailableException("Produto esgotado.");
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        // Agora que passou por todas as validações, cria e salva o Order já completo
        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> itemResponse = new ArrayList<>();

        for (OrderItemRequestDTO itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

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
        }

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
        Order order = orderRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Pedido não encontrado."));
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

    public OrderResponseDTO update(Long id, OrderStatusUpdateDTO request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));

        switch(order.getOrderStatus()) {
            case RECEIVED -> {}

            case PREPARING -> {
                List<OrderStatus> permitidos = List.of(OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED, OrderStatus.CANCELED);
                if (!permitidos.contains(request.orderStatus())) {
                    throw new InvalidOrderStatusException("Transição de status inválida.");
                }
            }

            case OUT_FOR_DELIVERY -> {
                List<OrderStatus> permitidos = List.of(OrderStatus.DELIVERED);
                if (!permitidos.contains(request.orderStatus())) {
                    throw new InvalidOrderStatusException("Transição de status inválida.");
                }
            }

            case DELIVERED -> {
                List<OrderStatus> permitidos = List.of(OrderStatus.CANCELED);
                if (!permitidos.contains(request.orderStatus())) {
                    throw new InvalidOrderStatusException("Transição de status inválida.");
                }
            }

            case CANCELED -> {
                throw new InvalidOrderStatusException("Transição de status inválida.");
            }

        }

        order.setOrderStatus(request.orderStatus());

        Order savedOrder = orderRepository.save(order);

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
                savedOrder.getId(),
                savedOrder.getCustomer().getId(),
                savedOrder.getAddress().getId(),
                itemResponse,
                savedOrder.getTotalPrice(),
                savedOrder.getOrderStatus(),
                savedOrder.getCreatedAt()
        );
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));
        orderRepository.delete(order);
    }
}