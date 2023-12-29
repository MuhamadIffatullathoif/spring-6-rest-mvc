package com.iffat.spring6.restmvc.services;

import com.iffat.spring6.restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
