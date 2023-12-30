package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.model.Customer;
import com.iffat.spring6.restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listAlLCustomer() {
        return customerService.getAllCustomers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{customerId}")
    public Customer getCustomerById(@PathVariable(name = "customerId") UUID id) {
        return customerService.getCustomerById(id);
    }

}
