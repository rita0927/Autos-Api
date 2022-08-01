package com.galvanize.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutosList> getAutos(@RequestParam(required = false) String color, @RequestParam(required = false) String make){
        AutosList autosList;
        if (color == null && make== null) {
            autosList = autosService.getAutos();
        } else {
            autosList = autosService.getAutos(color, make);
        }
        return autosList.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(autosList);
    }

    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile auto){
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> getAuto(@PathVariable String vin){
        Automobile automobile = autosService.getAuto(vin);
        return automobile == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(automobile);
    }

    @PatchMapping("/api/autos/{vin}")
    public Automobile updateAuto(@PathVariable String vin, @RequestBody UpdateRequest update){
        Automobile automobile = autosService.updateAuto(vin, update.getColor(),update.getOwner());
        automobile.setColor(update.getColor());
        automobile.setOwner(update.getOwner());
        return automobile;
    }

    @DeleteMapping("/api/autos/{vin}")
    public ResponseEntity deleteAuto(@PathVariable String vin) {
        try {
            autosService.deleteAuto(vin);
        } catch (AutoNotFoundException e){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.accepted().build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutoException e){

    }


}
