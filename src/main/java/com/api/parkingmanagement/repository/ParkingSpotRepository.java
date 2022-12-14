package com.api.parkingmanagement.repository;

import com.api.parkingmanagement.domain.ParkingSpotModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {

    boolean existsByApartmentAndBlock(String apartment, String block);

    Optional<ParkingSpotModel> findByApartmentAndBlock(String apartment, String block);

    Integer deleteByApartmentAndBlock(String apartment, String block);

    Page<ParkingSpotModel> findAll(Pageable pageable);
}
