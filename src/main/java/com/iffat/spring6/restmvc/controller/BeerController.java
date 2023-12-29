package com.iffat.spring6.restmvc.controller;

import com.iffat.spring6.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerService;
}
