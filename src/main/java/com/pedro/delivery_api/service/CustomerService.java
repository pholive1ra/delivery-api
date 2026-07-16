package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.CustomerRequestDTO;
import com.pedro.delivery_api.dto.CustomerResponseDTO;
import com.pedro.delivery_api.entity.Customer;
import com.pedro.delivery_api.exception.DuplicateResourceException;
import com.pedro.delivery_api.exception.ResourceNotFoundException;
import com.pedro.delivery_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDTO create (CustomerRequestDTO request) {
        String cleanPhone = request.phone().replaceAll("[^0-9]", "");

        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("E-mail já cadastrado.");
        }
        if (customerRepository.findByPhone(cleanPhone).isPresent()) {
            throw new DuplicateResourceException("Telefone já cadastrado.");
        }
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(cleanPhone);

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponseDTO(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getEmail(), savedCustomer.getPhone());
    }

   public List<CustomerResponseDTO> list() {
        List<Customer> listCustomer = customerRepository.findAll();

        return listCustomer.stream()
                .map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone()
                ))
                .toList();
    }

    public CustomerResponseDTO listById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return new CustomerResponseDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO request) {
        String cleanPhone = request.phone().replaceAll("[^0-9]", "");

        Customer customer = customerRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Cliente não encontrado."));
        customerRepository.findByEmail(request.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new DuplicateResourceException("E-mail já cadastrado.");
        });
        customerRepository.findByPhone(cleanPhone)
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new DuplicateResourceException("Telefone já cadastrado.");
                });

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(cleanPhone);

        Customer updatedCustomer = customerRepository.save(customer);

        return new CustomerResponseDTO(updatedCustomer.getId(), updatedCustomer.getName(), updatedCustomer.getEmail(), updatedCustomer.getPhone());
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        customerRepository.delete(customer);
    }
}
