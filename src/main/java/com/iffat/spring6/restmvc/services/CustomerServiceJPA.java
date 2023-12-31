package com.iffat.spring6.restmvc.services;

import com.iffat.spring6.restmvc.mappers.CustomerMapper;
import com.iffat.spring6.restmvc.model.CustomerDTO;
import com.iffat.spring6.restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomerServiceJPA implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(customer1 -> {
            customer1.setName(customer.getName());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer1))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO customer) {

    }
}
