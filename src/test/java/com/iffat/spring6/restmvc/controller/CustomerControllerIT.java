package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.entities.Customer;
import com.iffat.spring6.restmvc.mappers.CustomerMapper;
import com.iffat.spring6.restmvc.model.CustomerDTO;
import com.iffat.spring6.restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Rollback
    @Transactional
    @Test
    void testListEmptyCustomer() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listAlLCustomer();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listAlLCustomer();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("Customer Name")
                .build();

        ResponseEntity responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();
    }

    @Test
    void testUpdateById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);

        final String name = "UPDATED";
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setName(name);

        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(name);
    }

    @Test
    void testUpdateByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Test
    void testDeleteById() {
        Customer customer = customerRepository.findAll().get(0);
        customerController.deleteCustomerById(customer.getId());
        assertThat(customerRepository.findById(customer.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
}