package com.pedro.delivery_api.controller;
import com.pedro.delivery_api.dto.AddressRequestDTO;
import com.pedro.delivery_api.dto.AddressResponseDTO;
import com.pedro.delivery_api.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
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

    @PutMapping("/{id}")
    public AddressResponseDTO update(@PathVariable Long id, @RequestBody AddressRequestDTO request) {
        return addressService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
