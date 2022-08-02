package com.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {

    private AutosService autosService;

    @Mock
    AutosRepository autosRepository;

    @BeforeEach
    void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void getAutos_noArgs_returnsList() {
        // without setting up the return, autosList is an empty array (not null, but isEmpty)
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.findAll()).thenReturn(Arrays.asList(automobile));

        AutosList autosList = autosService.getAutos();
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();
    }

    @Test
    void getAutos_search_returnsList() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        automobile.setColor("red");
        when(autosRepository.findByColorAndMake(anyString(), anyString()))
                .thenReturn(Arrays.asList(automobile));

        AutosList autosList = autosService.getAutos("red", "Ford");
        assertThat(autosList).isNotNull();
        assertThat(autosList.isEmpty()).isFalse();

    }

    @Test
    void addAuto_valid_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.save(any(Automobile.class)))
                .thenReturn(automobile);
        Automobile auto = autosService.addAuto(automobile);
        assertThat(auto).isNotNull();
        assertThat(auto.getMake()).isEqualTo("Ford");
    }

    //how to test invalid addAuto???
    @Test
    void addAuto_invalid_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.save(any(Automobile.class)))
                .thenReturn(null);
        Automobile auto = autosService.addAuto(automobile);
        assertThat(auto).isNull();
    }

    @Test
    void getAuto_byVin_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        Automobile auto = autosService.getAuto("AABBCC");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void getAuto_byVin_notExists() {
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(AutoNotFoundException.class)
                .isThrownBy(() -> autosService.getAuto(anyString()));
    }

    @Test
    void updateAuto_valid_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        automobile.setColor("red");
        automobile.setOwner("Bob");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile auto = autosService.updateAuto(automobile.getVin(), "red", "Bob");
        assertThat(auto).isNotNull();
        assertThat(auto.getColor()).isEqualTo(automobile.getColor());
        assertThat(auto.getOwner()).isEqualTo(automobile.getOwner());
    }

    @Test
    void updateAuto_invalid_returnsNull() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.findByVin(anyString()))
//                .thenReturn(Optional.ofNullable(null));
                .thenReturn(Optional.empty());
        Automobile auto = autosService.updateAuto(automobile.getVin(), "red", "Bob");
        assertThat(auto).isNull();
    }

    @Test
    void deleteAuto_byVin() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustant", "AABBCC");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        autosService.deleteAuto(automobile.getVin());
        verify(autosRepository).delete(any(Automobile.class));
    }

    @Test
    void deleteAuto_byVin_notExists() {
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(AutoNotFoundException.class)
                .isThrownBy(() -> autosService.deleteAuto(anyString()));
    }
}