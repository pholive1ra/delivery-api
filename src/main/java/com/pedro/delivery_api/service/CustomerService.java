package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.CustomerRequestDTO;
import com.pedro.delivery_api.dto.CustomerResponseDTO;
import com.pedro.delivery_api.entity.Customer;
import com.pedro.delivery_api.exception.ResourceNotFoundException;
import com.pedro.delivery_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDTO create (CustomerRequestDTO request) {
        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceNotFoundException("E-mail já cadastrado.");
        }
        if (customerRepository.findByPhone(request.phone()).isPresent()) {
            throw new ResourceNotFoundException("Telefone já cadastrado.");
        }
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());

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
        Customer customer = customerRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Cliente não encontrado."));
        customerRepository.findByEmail(request.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ResourceNotFoundException("E-mail já cadastrado.");
        });
        customerRepository.findByPhone(request.phone())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ResourceNotFoundException("Telefone já cadastrado.");
                });

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());

        Customer updatedCustomer = customerRepository.save(customer);

        return new CustomerResponseDTO(updatedCustomer.getId(), updatedCustomer.getName(), updatedCustomer.getEmail(), updatedCustomer.getPhone());
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        customerRepository.delete(customer);
    }
}
