package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.domain.ParkingSpotModel;
import com.api.parkingmanagement.domain.requests.ParkingSpotRequest;
import com.api.parkingmanagement.repository.ParkingSpotRepository;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private ParkingSpotRepository parkingSpotRepository;

    ParkingSpotController(ParkingSpotRepository repository){
        this.parkingSpotRepository = repository;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index(){
        return "Helo bitch!";
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<ParkingSpotRequest> saveParkingSpot(@RequestBody @Valid ParkingSpotRequest body){

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(body, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parkingSpotRepository.save(parkingSpotModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
