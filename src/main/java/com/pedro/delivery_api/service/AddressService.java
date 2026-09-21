package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.request.AddressRequestDTO;
import com.pedro.delivery_api.dto.response.AddressResponseDTO;
import com.pedro.delivery_api.entity.Address;
import com.pedro.delivery_api.entity.Customer;
import com.pedro.delivery_api.entity.User;
import com.pedro.delivery_api.exception.InvalidOrderException;
import com.pedro.delivery_api.exception.ResourceNotFoundException;
import com.pedro.delivery_api.repository.AddressRepository;
import com.pedro.delivery_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressService(
            AddressRepository addressRepository,
            CustomerRepository customerRepository
    ) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    public AddressResponseDTO create(AddressRequestDTO request, User user) {

        Customer customer = customerRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        Address address = new Address();

        address.setCustomer(customer);
        address.setCity(request.city());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setZipCode(request.zipCode());
        address.setStreet(request.street());

        Address savedAddress = addressRepository.save(address);

        return new AddressResponseDTO(
                savedAddress.getId(),
                savedAddress.getStreet(),
                savedAddress.getNumber(),
                savedAddress.getComplement(),
                savedAddress.getNeighborhood(),
                savedAddress.getCity(),
                savedAddress.getZipCode(),
                savedAddress.getCustomer().getId()
        );
    }

    public List<AddressResponseDTO> list(User user) {

        Customer customer = customerRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        List<Address> listAddress =
                addressRepository.findByCustomerId(customer.getId());

        return listAddress.stream()
                .map(address -> new AddressResponseDTO(
                        address.getId(),
                        address.getStreet(),
                        address.getNumber(),
                        address.getComplement(),
                        address.getNeighborhood(),
                        address.getCity(),
                        address.getZipCode(),
                        address.getCustomer().getId()
                ))
                .collect(Collectors.toList());
    }

    public AddressResponseDTO listById(Long id, User user) {

        Customer customer = customerRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Endereço não encontrado."));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidOrderException(
                    "Esse endereço não pertence ao cliente autenticado."
            );
        }

        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getZipCode(),
                address.getCustomer().getId()
        );
    }

    public AddressResponseDTO update(
            Long id,
            AddressRequestDTO request,
            User user
    ) {

        Customer customer = customerRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Endereço não encontrado."));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidOrderException(
                    "Esse endereço não pertence ao cliente autenticado."
            );
        }

        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setZipCode(request.zipCode());

        Address savedAddress = addressRepository.save(address);

        return new AddressResponseDTO(
                savedAddress.getId(),
                savedAddress.getStreet(),
                savedAddress.getNumber(),
                savedAddress.getComplement(),
                savedAddress.getNeighborhood(),
                savedAddress.getCity(),
                savedAddress.getZipCode(),
                savedAddress.getCustomer().getId()
        );
    }

    public void delete(Long id, User user) {

        Customer customer = customerRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        Address address = addressRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Endereço não encontrado."));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidOrderException(
                    "Esse endereço não pertence ao cliente autenticado."
            );
        }

        addressRepository.delete(address);
    }
}