package com.api.parkingmanagement.resources;

import com.api.parkingmanagement.domain.ParkingSpotModel;
import com.api.parkingmanagement.domain.requests.ParkingSpotRequest;
import com.api.parkingmanagement.domain.requests.ParkingStopUpdateRequest;
import com.api.parkingmanagement.domain.responses.ParkingSpotPageableResponse;
import com.api.parkingmanagement.domain.responses.ParkingSpotResponse;
import com.api.parkingmanagement.repository.ParkingSpotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/parking-spot")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @RequestMapping(method = RequestMethod.POST)
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
    ResponseEntity<ParkingSpotPageableResponse> getAllParkingSpot(@PageableDefault(page = 0, size = 1, sort = "responsibleName", direction = Sort.Direction.ASC) Pageable pageable) {

        ParkingSpotPageableResponse pageableResponse = new ParkingSpotPageableResponse();
        Page<ParkingSpotModel> parkingSpotModelPage = parkingSpotRepository.findAll(pageable);
        if (parkingSpotModelPage.toList().size() < 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        pageableResponse.setPageNumber(parkingSpotModelPage.getPageable().getPageNumber());
        pageableResponse.setTotalElements(parkingSpotModelPage.getTotalElements());
        pageableResponse.setTotalPages(parkingSpotModelPage.getTotalPages());
        pageableResponse.setPageSize(parkingSpotModelPage.getPageable().getPageSize());
        pageableResponse.setOffSet(parkingSpotModelPage.getPageable().getOffset());
        pageableResponse.setFirst(parkingSpotModelPage.isFirst());
        parkingSpotModelPage.toList().forEach(parkingSpotModel -> {
            ParkingSpotResponse response = new ParkingSpotResponse();
            BeanUtils.copyProperties(parkingSpotModel, response);
            pageableResponse.getLstParkingSpotResponse().add(response);
        });

        return ResponseEntity.ok().body(pageableResponse);
    }

    @RequestMapping(value = "/{apartment}/{block}", method = RequestMethod.GET)
    ResponseEntity<ParkingSpotResponse> getParkingSpot(
            @PathVariable(value = "apartment") String apartment, @PathVariable(value = "block") String block) {

        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotRepository.findByApartmentAndBlock(apartment, block);
        if (!optionalParkingSpotModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        ParkingSpotResponse response = new ParkingSpotResponse();
        BeanUtils.copyProperties(optionalParkingSpotModel.get(), response);

        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @RequestMapping(value = "/{apartment}/{block}", method = RequestMethod.DELETE)
    public ResponseEntity deleteParkingSpots(
            @PathVariable(value = "apartment") String apartment, @PathVariable(value = "block") String block) {

        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotRepository.findByApartmentAndBlock(apartment, block);
        if (!optionalParkingSpotModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (parkingSpotRepository.deleteByApartmentAndBlock(apartment, block) < 0)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok().body("ParkingSpot Deleted! apartment:" + apartment + " block:" + block);
    }

    @RequestMapping(value = "/{apartment}/{block}", method = RequestMethod.PUT)
    ResponseEntity<ParkingStopUpdateRequest> updateParkingSpots(
            @PathVariable(value = "apartment") String apartment, @PathVariable(value = "block") String block,
            @RequestBody @Valid ParkingStopUpdateRequest body) {

        Optional<ParkingSpotModel> optionalParkingSpotModel = parkingSpotRepository.findByApartmentAndBlock(apartment, block);
        ParkingSpotModel parkingSpotModel;

        try {
            parkingSpotModel = optionalParkingSpotModel.orElseThrow(() -> new IllegalArgumentException("Apartment and Block Not Found!"));
            BeanUtils.copyProperties(body, parkingSpotModel);
            parkingSpotRepository.save(parkingSpotModel);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(body);
    }
}
