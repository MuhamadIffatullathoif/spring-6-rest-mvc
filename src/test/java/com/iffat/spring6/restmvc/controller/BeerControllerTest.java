package com.iffat.spring6.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iffat.spring6.restmvc.config.SpringSecurityConfig;
import com.iffat.spring6.restmvc.model.BeerDTO;
import com.iffat.spring6.restmvc.services.BeerService;
import com.iffat.spring6.restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    public static final String USERNAME = "user1";
    public static final String PASSWORD = "password";

    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor =
            jwt().jwt(jwt -> {
                // jwt.claims(claim -> {
                //    claim.put("scope", "message-read");
                //    claim.put("scope","message-write");
                // })
                jwt.claims(Map::clear)
                        .subject("oidc-client")
                        .notBefore(Instant.now().minusSeconds(5L));
            });

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testPatchBeers() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap))
                        .with(jwt().jwt(jwt -> {
                            // jwt.claims(claim -> {
                            //    claim.put("scope", "message-read");
                            //    claim.put("scope","message-write");
                            // })
                            jwt.claims(Map::clear)
                                    .subject("oidc-client")
                                    .notBefore(Instant.now().minusSeconds(5L));
                        })))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeers() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.deleteBeerById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeers() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testUpdateBeersBlankName() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("");
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
                        .with(jwtRequestPostProcessor))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateNewBeers() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setId(null);
        beer.setVersion(null);

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.listBeers(null, null, false, null, null));

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .with(jwtRequestPostProcessor))
                .andExpect(jsonPath("$.length()", is(6)))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}