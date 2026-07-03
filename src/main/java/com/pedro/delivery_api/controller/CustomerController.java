package com.pedro.delivery_api.controller;

import com.pedro.delivery_api.dto.CustomerRequestDTO;
import com.pedro.delivery_api.dto.CustomerResponseDTO;
import com.pedro.delivery_api.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController (CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("")
    public CustomerResponseDTO create(@RequestBody CustomerRequestDTO request) {
        return customerService.create(request);
    }

    @GetMapping("")
    public List<CustomerResponseDTO> list() {
        return customerService.list();
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO listById(@PathVariable Long id) {
        return customerService.listById(id);
    }

    @PutMapping("/{id}")
    public CustomerResponseDTO update(@PathVariable Long id, @RequestBody CustomerRequestDTO request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }
}
