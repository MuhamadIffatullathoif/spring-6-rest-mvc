package com.iffat.spring6.restmvc.repositories;

import com.iffat.spring6.restmvc.entities.Beer;
import com.iffat.spring6.restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

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