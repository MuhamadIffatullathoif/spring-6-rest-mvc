package com.iffat.spring6.restmvc.repositories;

import com.iffat.spring6.restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;


    @Test
    void testSaveCustomers() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
                .name("new name")
                .build());

        assertThat(savedCustomer).isNotNull();
    }
}