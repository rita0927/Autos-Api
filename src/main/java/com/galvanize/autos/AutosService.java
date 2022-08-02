package com.galvanize.autos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutosService {

    AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {
        return new AutosList(autosRepository.findAll());
    }

    public AutosList getAutos(String color, String make) {
        List<Automobile> automobiles = autosRepository.findByColorAndMake(color, make);
//        if (!automobiles.isEmpty()){
//            return new AutosList(automobiles);
//        }
//        return null;
        return automobiles.isEmpty()? null: new AutosList(automobiles);
    }

    public Automobile addAuto(Automobile auto) {
        return autosRepository.save(auto);
    }

    public Automobile getAuto(String vin) {
//        return autosRepository.findByVin(vin).orElse(null);

        Optional<Automobile> optAuto = autosRepository.findByVin(vin);
        if (optAuto.isPresent()){
            return optAuto.get();
        } else {
            throw new AutoNotFoundException();
        }
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        Optional<Automobile> optAuto = autosRepository.findByVin(vin);
        // if optAuto is not null
        if (optAuto.isPresent()){
            //get automobile from Optional class first
            optAuto.get().setColor(color);
            optAuto.get().setOwner(owner);
            return autosRepository.save(optAuto.get());
        }
        return null;
    }

    public void deleteAuto(String vin){
        Optional<Automobile> optAuto = autosRepository.findByVin(vin);
        if(optAuto.isPresent()){
            autosRepository.delete(optAuto.get());
        } else {
            throw new AutoNotFoundException();
        }
    }
}
