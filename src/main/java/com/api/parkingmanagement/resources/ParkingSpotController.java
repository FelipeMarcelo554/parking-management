package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.domain.ParkingSpotModel;
import com.api.parkingmanagement.domain.requests.ParkingSpotRequest;
import com.api.parkingmanagement.domain.responses.ParkingSpotResponse;
import com.api.parkingmanagement.repository.ParkingSpotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/parking-spot")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @PostMapping
    ResponseEntity<?> saveParkingSpot(@RequestBody @Valid ParkingSpotRequest body) {

        if (parkingSpotRepository.existsByApartmentAndBlock(body.getApartment(), body.getBlock()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Apartment and block has been already registered!");

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(body, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parkingSpotRepository.save(parkingSpotModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<ParkingSpotResponse>> getParkingSpots() {

        List<ParkingSpotResponse> lstResponse = new ArrayList<>();
        List<ParkingSpotModel> lstParkingSpot = parkingSpotRepository.findAll();
        lstParkingSpot.forEach(parkingSpotModel -> {

            ParkingSpotResponse response = new ParkingSpotResponse();
            BeanUtils.copyProperties(parkingSpotModel, response);
            lstResponse.add(response);
        });

        return ResponseEntity.ok().body(lstResponse);
    }

    @RequestMapping(value = "/{apartment}/{block}", method = RequestMethod.GET)
    ResponseEntity<ParkingSpotResponse> getParkingSpots(
            @PathVariable(value = "apartment") String apartment, @PathVariable(value = "block") String block) {

        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotRepository.findByApartmentAndBlock(apartment, block);
        if (!optionalParkingSpotModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        ParkingSpotResponse response = new ParkingSpotResponse();
        BeanUtils.copyProperties(optionalParkingSpotModel.get(), response);

        return ResponseEntity.ok().body(response);
    }
}
