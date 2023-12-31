package com.iffat.spring6.restmvc.services;

import com.iffat.spring6.restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID bearId, BeerDTO beer);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDTO beer);
}
