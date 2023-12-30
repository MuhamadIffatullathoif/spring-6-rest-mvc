package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.model.Beer;
import com.iffat.spring6.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{beerId}")
    public Beer getBeerById(@PathVariable UUID beerId) {
        log.debug("Get Beer id - in controller");
        return beerService.getBeerById(beerId);
    }
}
