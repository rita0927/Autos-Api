package com.galvanize.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    // Get: /api/autos

        // GET: /api/autos Returns list of all autos
        @Test
        void getAuto_noParams_exits_returnsAutosLists() throws Exception{

            List<Automobile> automobiles = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                automobiles.add(new Automobile(2000+i, "Ford", "Mustang", "AABB"+i));
            }

            when(autosService.getAutos()).thenReturn(new AutosList(automobiles));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/autos"))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.automobiles", hasSize(5)));

        }
        // GET: /api/autos Returns 204 if no autos in the db


        // GET: /api/autos?color=red returns red cars
        // GET: /api/autos?make=ford returns Ford cars
        // GET: /api/autos?color=red&make=ford returns red Ford cars

    // POST: //api/autos
        // POST: /api/autos returns status code 400
        // POST: /api/autos returns status code 200 and created auto

    // GET: /api/autos/{vin}
        // GET: /api/autos/{vin} returns status code 204 Auto not found
        // GET: /api/autos/{vin} returns status code 200 and the requested auto

    // PATCH: /api/autos/{vin}
        // PATCH: /api/autos/{vin} returns status code 204 Auto not found
        // PATCH: /api/autos returns status code 400
        // PATCH: /api/autos/{vin} returns status code 200 and patched auto

    // DELETE: /api/autos/{vin}
        // DELETE: /api/autos/{vin} returns status code 204 Auto not found
        // DELETE: /api/autos/{vin} returns status code 202, delete request accepted





}
