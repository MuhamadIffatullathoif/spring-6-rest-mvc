package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.entities.Customer;
import com.iffat.spring6.restmvc.model.CustomerDTO;
import com.iffat.spring6.restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}