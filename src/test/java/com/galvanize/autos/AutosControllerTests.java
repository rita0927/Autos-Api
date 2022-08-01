package com.galvanize.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

    // Get: /api/autos

        // GET: /api/autos Returns list of all autos
        @Test
        void getAuto_noParams_exits_returnsAutosLists() throws Exception{

            List<Automobile> automobiles = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                automobiles.add(new Automobile(2000+i, "Ford", "Mustang", "AABB"+i));
            }

            when(autosService.getAutos()).thenReturn(new AutosList(automobiles));

            mockMvc.perform(get("/api/autos"))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.automobiles", hasSize(5)));

        }

        // GET: /api/autos Returns 204 if no autos in the db
        @Test
        void getAutos_noParams_none_returnsNoContent() throws Exception {
            when(autosService.getAutos()).thenReturn(new AutosList());
            mockMvc.perform(get("/api/autos"))
//                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

    // GET: /api/autos?color=red&make=ford returns red Ford cars
        @Test
        void getAutos_searchParams_exists_returnsAutosList() throws Exception{

            List<Automobile> automobiles = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Automobile automobile = new Automobile(2000+i, "Ford", "Mustang", "AABB"+i);
                automobile.setColor("red");
                automobiles.add(automobile);
            }

            when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
            mockMvc.perform(get("/api/autos?color=red&make=ford"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.automobiles", hasSize(5)));
        }


        // GET: /api/autos?color=red returns red cars
        @Test
        void getAutos_searchColor_exists_returnsAutosList() throws Exception{
            List<Automobile> automobiles = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Automobile automobile = new Automobile(2000+i, "Ford", "Mustang", "AABB"+i);
                automobile.setColor("red");
                automobiles.add(automobile);
            }

            when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
            mockMvc.perform(get("/api/autos/?color=red&make=null"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.automobiles", hasSize(5)));

        }

        // GET: /api/autos?make=ford returns Ford cars

    @Test
    void getAutos_searchMake_exists_returnsAutosList() throws Exception{
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(2000+i, "Ford", "Mustang", "AABB"+i));
        }

        when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos/?color=null&make=ford"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));

    }


    // POST: //api/autos
        // POST: /api/autos returns status code 200 and created auto
    @Test
    void addAuto_valid_returnsAuto() throws Exception {
            Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
            String json = " {\"year\":1967,\"make\":\"Ford\",\"model\":\"Mustant\",\"color\":null,\"owner\":null,\"vin\":\"AABBCC\"}";
            when(autosService.addAuto(any(Automobile.class))).thenReturn(automobile);
            mockMvc.perform(post("/api/autos")
                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(mapper.writeValueAsString(automobile)))
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("make").value("Ford"));

    }

    // POST: /api/autos returns status code 400
    @Test
    void addAuto_badRequ_returns400() throws Exception{
            when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
            mockMvc.perform(post("/api/autos"))
                    .andExpect(status().isBadRequest());
    }


    // GET: /api/autos/{vin}

        // GET: /api/autos/{vin} returns status code 200 and the requested auto
    @Test
    void getAuto_withVin_returnsAuto()throws Exception{
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosService.getAuto(anyString())).thenReturn(automobile);
        mockMvc.perform(get("/api/autos/"+automobile.getVin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value(automobile.getVin()));

    }

        // GET: /api/autos/{vin} returns status code 204 Auto not found
    @Test
    void getAutos_withVin_returnsNoContent() throws Exception {
        when(autosService.getAuto(anyString())).thenReturn(null);
        mockMvc.perform(get("/api/autos/AABBCC"))
                .andExpect(status().isNoContent());
    }

    // PATCH: /api/autos/{vin}
        // PATCH: /api/autos/{vin} returns status code 200 and patched auto
    @Test
    void updateAuto_withObject_returnAuto() throws Exception{
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosService.updateAuto(anyString(), anyString(), anyString())).thenReturn(automobile);
        mockMvc.perform(patch("/api/autos/"+automobile.getVin())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"red\",\"owner\":\"Bob\"}"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("color").value("red"))
                .andExpect(jsonPath("owner").value("Bob"));
    }
        // PATCH: /api/autos/{vin} returns status code 204 Auto not found
        @Test
        void updateAutos_withVin_returnsNoContent() throws Exception {
            when(autosService.updateAuto(anyString(), anyString(), anyString())).thenThrow(AutoNotFoundException.class);
            mockMvc.perform(patch("/api/autos/AABBCC")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"color\":\"red\",\"owner\":\"Bob\"}"))
                    .andExpect(status().isNoContent());
        }

        // PATCH: /api/autos returns status code 400
        @Test
        void updateAuto_badRequ_returns400() throws Exception{
            when(autosService.updateAuto(anyString(), anyString(), anyString())).thenThrow(InvalidAutoException.class);
            mockMvc.perform(patch("/api/autos/AABBCC"))
                    .andExpect(status().isBadRequest());
        }


    // DELETE: /api/autos/{vin}
        // DELETE: /api/autos/{vin} returns status code 202, delete request accepted
    @Test
    void deleteAuto_withVin_exists_returns202() throws Exception{
            mockMvc.perform(delete("/api/autos/AABBCC"))
                    .andExpect(status().isAccepted());
            verify(autosService).deleteAuto(anyString());
    }

        // DELETE: /api/autos/{vin} returns status code 204 Auto not found
    @Test
    void deleteAutos_withVin_returnsNoContent() throws Exception{
            doThrow(new AutoNotFoundException()).when(autosService).deleteAuto(anyString());
            mockMvc.perform(delete("/api/autos/AABBCC"))
                    .andExpect(status().isNoContent());
    }


}
