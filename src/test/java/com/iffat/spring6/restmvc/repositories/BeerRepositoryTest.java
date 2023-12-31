package com.iffat.spring6.restmvc.repositories;

import com.iffat.spring6.restmvc.bootstrap.BootstrapData;
import com.iffat.spring6.restmvc.entities.Beer;
import com.iffat.spring6.restmvc.model.BeerStyle;
import com.iffat.spring6.restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        List<Beer> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");

        assertThat(list.size()).isEqualTo(336);
    }

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .beerName("My Beer 12345123451234512345123451234512345123451234512345123451234512345123451234512345")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("232323")
                    .price(new BigDecimal("11.29"))
                    .build());

            beerRepository.flush();
        });
    }

    @Test
    void testSaveBeers() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("232323")
                .price(new BigDecimal("11.29"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}