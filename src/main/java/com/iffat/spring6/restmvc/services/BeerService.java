package com.iffat.spring6.restmvc.services;

import com.iffat.spring6.restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> listBeers();
    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);
}
