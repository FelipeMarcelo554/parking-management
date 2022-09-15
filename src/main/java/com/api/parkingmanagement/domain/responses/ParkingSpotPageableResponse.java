package com.api.parkingmanagement.domain.responses;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpotPageableResponse {

    private Integer pageNumber;
    private Long totalElements;
    private Integer totalPages;
    private Integer pageSize;
    private Long offSet;
    private boolean first;
    private List<ParkingSpotResponse> lstParkingSpotResponse = new ArrayList<>();

    public ParkingSpotPageableResponse() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getOffSet() {
        return offSet;
    }

    public void setOffSet(Long offSet) {
        this.offSet = offSet;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public List<ParkingSpotResponse> getLstParkingSpotResponse() {
        return lstParkingSpotResponse;
    }

    public void setLstParkingSpotResponse(List<ParkingSpotResponse> lstParkingSpotResponse) {
        this.lstParkingSpotResponse = lstParkingSpotResponse;
    }
}
