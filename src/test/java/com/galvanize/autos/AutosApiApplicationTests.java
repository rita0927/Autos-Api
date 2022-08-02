package com.galvanize.autos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class AutosApiApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AutosRepository autosRepository;

    Random r = new Random();
    List<Automobile> testAutos;

    @BeforeEach
    void setUp(){
        this.testAutos = new ArrayList<>();
        Automobile auto;
        String[] colors = {"red", "blue", "green", "pink"};

        for (int i = 0; i < 20; i++) {
            if(i%3 == 0){
                auto = new Automobile(1967, "Ford", "Mustang", "FFBBCC"+(i*13));
                auto.setColor(colors[r.nextInt(4)]);
            } else if (i%2 == 0){
                auto = new Automobile(2000, "Dodge", "Viper", "DDBBCC"+(i*12));
                auto.setColor(colors[r.nextInt(4)]);
            } else {
                auto = new Automobile(2020, "Audi", "Quatro", "AABBCC" + (i * 12));
                auto.setColor(colors[r.nextInt(4)]);
            }
            this.testAutos.add(auto);
        }

        autosRepository.saveAll(this.testAutos);
    }

    @AfterEach
    void tearDown(){
        autosRepository.deleteAll();
    }

	@Test
	void contextLoads() {
	}

    @Test
    void getAutos_exits_returnsAutosList(){
        ResponseEntity<AutosList> response = restTemplate.getForEntity("/api/autos", AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
//        for(Automobile auto: response.getBody().getAutomobiles()){
//            System.out.println(auto);
//        }
    }

    @Test
    void getAutos_search_returnsAutosList(){
        int seq = r.nextInt(20);
        String color = testAutos.get(seq).getColor();
        String make = testAutos.get(seq).getMake();
        ResponseEntity<AutosList> response = restTemplate.getForEntity(String.format("/api/autos?color=%s&make=%s", color, make), AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
//        for(Automobile auto: response.getBody().getAutomobiles()){
//            System.out.println(auto);
//        }

    }

    @Test
    void addAuto_returnsNewAuto(){
        Automobile automobile = new Automobile(1995, "Ford", "Windstar", "FFBBCC");
        automobile.setColor("yellow");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

        ResponseEntity<Automobile> response = restTemplate.postForEntity("/api/autos", request, Automobile.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());

    }


}
