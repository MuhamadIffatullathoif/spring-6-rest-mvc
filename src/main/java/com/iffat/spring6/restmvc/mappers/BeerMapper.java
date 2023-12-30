package com.iffat.spring6.restmvc.mappers;

import com.iffat.spring6.restmvc.entities.Beer;
import com.iffat.spring6.restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDto(Beer beer);
}
