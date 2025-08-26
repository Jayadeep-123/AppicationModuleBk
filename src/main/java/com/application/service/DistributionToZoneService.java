package com.application.service;

import com.application.dto.DistributionRequestDTO;
import com.application.entity.*;
import com.application.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistributionToZoneService {

    private final DistributionRepository distributionRepository;
    private final BalanceTrackRepository balanceTrackRepository;
    private final AcademicYearRepository academicYearRepository;
    private final EmployeeRepository employeeRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final ZoneRepository zoneRepository;
    private final AppIssuedTypeRepository appIssuedTypeRepository;
    private final DistrictRepository districtRepository;

    @Transactional
    public void createDistributionAndBalance(DistributionRequestDTO request) {

        // --- 1. Fetch all related entities ---
        Employee issuedToEmployee = employeeRepository.findById(request.getIssuedToEmpId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> new EntityNotFoundException("State not found"));
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new EntityNotFoundException("Zone not found"));
        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new EntityNotFoundException("Academic Year not found"));
        AppIssuedType issuedByType = appIssuedTypeRepository.findById(request.getIssuedByTypeId())
                .orElseThrow(() -> new EntityNotFoundException("IssuedByType not found"));
        AppIssuedType issuedToType = appIssuedTypeRepository.findById(request.getIssuedToTypeId())
                .orElseThrow(() -> new EntityNotFoundException("IssuedToType not found"));
        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new EntityNotFoundException("District not found"));

        // ... (Logic for parsing application number remains the same) ...
        String appNoFromStr = request.getApplicationNoFrom();
        String[] parts = appNoFromStr.split("-");
        String numericPartStr = parts[parts.length - 1];
        long startNoNumeric = Long.parseLong(numericPartStr);
        long endNoNumeric = startNoNumeric + request.getRange() - 1;
        String prefix = appNoFromStr.substring(0, appNoFromStr.lastIndexOf("-") + 1);
        String appNoToStr = prefix + String.format("%0" + numericPartStr.length() + "d", endNoNumeric);

        // --- 3. Create and Save the Distribution Entity ---
        Distribution distribution = new Distribution();
        
        distribution.setAcademicYear(academicYear);
        distribution.setIssuedByType(issuedByType);
        distribution.setIssuedToType(issuedToType);
        distribution.setDistrict(district);
        distribution.setAppStartNo(appNoFromStr);
        distribution.setAppEndNo(appNoToStr);
        distribution.setTotalAppCount(request.getRange());
        distribution.setIssued_to_emp_id(issuedToEmployee.getEmp_id());
        distribution.setState(state);
        distribution.setCity(city);
        distribution.setZone(zone);
        
        int loggedInUserId = 1;
        distribution.setCreated_by(loggedInUserId);
        distribution.setIsActive(1);

        distributionRepository.save(distribution);

        // --- 4. Create and Save the BalanceTrack Entity ---
        BalanceTrack balanceTrack = new BalanceTrack();
        
        balanceTrack.setAppAvblCnt(request.getRange());
        balanceTrack.setAppFrom((int) startNoNumeric);
        balanceTrack.setAppTo((int) endNoNumeric);
        balanceTrack.setAcademicYear(academicYear);
        balanceTrack.setEmployee(issuedToEmployee);
        balanceTrack.setCreatedBy(loggedInUserId);
        balanceTrack.setIsActive(1);

        // --- THIS IS THE FINAL REQUIRED LINE ---
        balanceTrack.setIssuedByType(issuedByType); 

        balanceTrackRepository.save(balanceTrack);
    }
}
