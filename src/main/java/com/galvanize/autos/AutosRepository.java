package com.galvanize.autos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutosRepository extends JpaRepository<Automobile, Long> {
    List<Automobile> findByColorAndMake(String color, String make);
    //Optional can hold up to one value and gracefully deals with null values
    Optional<Automobile> findByVin(String vin);


}
