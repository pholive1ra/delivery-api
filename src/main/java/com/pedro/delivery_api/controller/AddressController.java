package com.pedro.delivery_api.controller;

import com.pedro.delivery_api.dto.AddressRequestDTO;
import com.pedro.delivery_api.dto.AddressResponseDTO;
import com.pedro.delivery_api.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    private AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("")
    public AddressResponseDTO create(@RequestBody AddressRequestDTO request) {
        return addressService.create(request);
    }

    @GetMapping("")
    public List<AddressResponseDTO> list() {
        return addressService.list();
    }

    @GetMapping("/{id}")
    public AddressResponseDTO listById(@PathVariable Long id) {
        return addressService.listById(id);
    }
}
