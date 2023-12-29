package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.model.Beer;
import com.iffat.spring6.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        log.debug("Get Beer id - in controller");
        return beerService.getBeerById(id);
    }
}
