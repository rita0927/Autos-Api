package com.galvanize.autos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutosRepository extends JpaRepository<Automobile, Long> {
    List<Automobile> findByColorAndMake(String color, String make);


}
