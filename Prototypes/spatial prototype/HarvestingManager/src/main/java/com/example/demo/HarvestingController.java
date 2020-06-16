package com.example.demo;

import model.HarvestingLocationList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
public class HarvestingController {
    HarvestingService harvestingService;

    public HarvestingController(HarvestingService harvestingService){
        this.harvestingService=harvestingService;
    }

    @GetMapping("/harvestinglocations")
    public ResponseEntity<HarvestingLocationList> getAll(){
        return new ResponseEntity<>(harvestingService.getHarvestingLocationList(), HttpStatus.OK);
    }

    @PostMapping("/harvestinglocations/{px}/{py}/{nx}/{ny}")
    public ResponseEntity<HarvestingLocation> addLocation(
            @PathVariable double px,
            @PathVariable double py,
            @PathVariable double nx,
            @PathVariable double ny){
        HarvestingLocation hl = harvestingService.addLocation(px,py,nx,ny);
        return new ResponseEntity<>(hl, HttpStatus.OK);
    }

    @DeleteMapping("/harvestinglocations/{px}/{py}/{nx}/{ny}")
    public void removeLocation(
            @PathVariable double px,
            @PathVariable double py,
            @PathVariable double nx,
            @PathVariable double ny){
        harvestingService.removeLocation(px, py, nx, ny);
    }

    @PostMapping("/tasks/harvestdata/{day}")
    public void performDataHarvesting(@PathVariable int day){
        harvestingService.performHarvesting(day);
    }
}
