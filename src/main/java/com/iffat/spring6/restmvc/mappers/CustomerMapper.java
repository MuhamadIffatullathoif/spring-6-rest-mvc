package com.iffat.spring6.restmvc.mappers;

import com.iffat.spring6.restmvc.entities.Customer;
import com.iffat.spring6.restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDto(Customer customer);
}
