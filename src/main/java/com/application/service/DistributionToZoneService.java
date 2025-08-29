//package com.application.service;
//
//import com.application.dto.DistributionRequestDTO;
//import com.application.entity.*;
//import com.application.repository.*;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class DistributionToZoneService {
//
//    private final DistributionRepository distributionRepository;
//    private final BalanceTrackRepository balanceTrackRepository;
//    private final AcademicYearRepository academicYearRepository;
//    private final EmployeeRepository employeeRepository;
//    private final StateRepository stateRepository;
//    private final CityRepository cityRepository;
//    private final ZoneRepository zoneRepository;
//    private final AppIssuedTypeRepository appIssuedTypeRepository;
//    private final DistrictRepository districtRepository;
//    // --- Injected the new repository ---
//    private final StateAppRepository stateAppRepository;
//
//    @Transactional
//    public void createDistributionAndBalance(DistributionRequestDTO request) {
//
//        // --- 1. Fetch related entities ---
//        Employee issuedToEmployee = employeeRepository.findById(request.getIssuedToEmpId())
//                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
//        State state = stateRepository.findById(request.getStateId())
//                .orElseThrow(() -> new EntityNotFoundException("State not found"));
//        City city = cityRepository.findById(request.getCityId())
//                .orElseThrow(() -> new EntityNotFoundException("City not found"));
//        Zone zone = zoneRepository.findById(request.getZoneId())
//                .orElseThrow(() -> new EntityNotFoundException("Zone not found"));
//        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
//                .orElseThrow(() -> new EntityNotFoundException("Academic Year not found"));
//        AppIssuedType issuedByType = appIssuedTypeRepository.findById(request.getIssuedByTypeId())
//                .orElseThrow(() -> new EntityNotFoundException("IssuedByType not found"));
//        AppIssuedType issuedToType = appIssuedTypeRepository.findById(request.getIssuedToTypeId())
//                .orElseThrow(() -> new EntityNotFoundException("IssuedToType not found"));
//        District district = districtRepository.findById(request.getDistrictId())
//                .orElseThrow(() -> new EntityNotFoundException("District not found"));
//
//        int loggedInUserId = 1; // Placeholder for actual logged-in user ID
//
//        // --- 2. Determine the next available application start number ---
//        StateApp stateApp = stateAppRepository.findByState_StateIdAndAcademicYear_AcdcYearIdAndCreated_by(state.getStateId(), academicYear.getAcdcYearId(), loggedInUserId)
//                .orElseThrow(() -> new IllegalStateException("Master application range for the selected state and year is not defined for this user."));
//
//        Optional<Distribution> lastDistributionOpt = distributionRepository
//            .findTopByState_StateIdAndAcademicYear_AcdcYearIdOrderByAppEndNoDesc(state.getStateId(), academicYear.getAcdcYearId());
//
//        long startNoNumeric;
//        if (lastDistributionOpt.isPresent()) {
//            // If a distribution exists, the next number is the one after the last one
//            String lastEndNoStr = lastDistributionOpt.get().getAppEndNo();
//            long lastEndNo = Long.parseLong(lastEndNoStr.substring(lastEndNoStr.lastIndexOf("-") + 1));
//            startNoNumeric = lastEndNo + 1;
//        } else {
//            // If this is the very first distribution, start from the beginning of the master range
//            startNoNumeric = stateApp.getApp_start_no();
//        }
//        
//        long endNoNumeric = startNoNumeric + request.getRange() - 1;
//
//        // --- 3. Validate against Master Range ---
//        if (endNoNumeric > stateApp.getApp_end_no()) {
//            throw new IllegalStateException("Distribution exceeds the maximum available application number for this state. Last available number is " + stateApp.getApp_end_no());
//        }
//
//        // --- 4. Find or Create/Update Recipient's Balance ---
//        Optional<BalanceTrack> existingBalanceOpt = balanceTrackRepository
//                .findByEmployeeAndAcademicYear(issuedToEmployee, academicYear);
//
//        if (existingBalanceOpt.isPresent()) {
//            BalanceTrack existingBalance = existingBalanceOpt.get();
//            existingBalance.setAppTo((int) endNoNumeric);
//            existingBalance.setAppAvblCnt(existingBalance.getAppAvblCnt() + request.getRange());
//            balanceTrackRepository.save(existingBalance);
//        } else {
//            BalanceTrack newBalance = new BalanceTrack();
//            newBalance.setAppAvblCnt(request.getRange());
//            newBalance.setAppFrom((int) startNoNumeric);
//            newBalance.setAppTo((int) endNoNumeric);
//            newBalance.setAcademicYear(academicYear);
//            newBalance.setEmployee(issuedToEmployee);
//            newBalance.setCreatedBy(loggedInUserId);
//            newBalance.setIsActive(1);
//            newBalance.setIssuedByType(issuedByType); 
//            balanceTrackRepository.save(newBalance);
//        }
//
//        // --- 5. Create Distribution Log ---
//        // We need a sample 'from' number to get the prefix, e.g., "APP-2025-"
//        // In a real system, this prefix might be stored in the StateApp table.
//        String prefix = "APP-2025-"; 
//        String numericPartExample = "0000"; // To determine padding length
//        String appNoFromStr = prefix + String.format("%0" + numericPartExample.length() + "d", startNoNumeric);
//        String appNoToStr = prefix + String.format("%0" + numericPartExample.length() + "d", endNoNumeric);
//
//        Distribution distribution = new Distribution();
//        distribution.setAcademicYear(academicYear);
//        distribution.setIssuedByType(issuedByType);
//        distribution.setIssuedToType(issuedToType);
////        distribution.setDistrict(district);
//        distribution.setAppStartNo(appNoFromStr);
//        distribution.setAppEndNo(appNoToStr);
//        distribution.setTotalAppCount(request.getRange());
//        distribution.setIssued_to_emp_id(issuedToEmployee.getEmp_id());
//        distribution.setState(state);
//        distribution.setCity(city);
//        distribution.setZone(zone);
//        distribution.setCreated_by(loggedInUserId);
//        distribution.setIsActive(1);
//        distributionRepository.save(distribution);
//    }
//
//	public String calculateNextApplicationNo(Integer stateId, Integer academicYearId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
