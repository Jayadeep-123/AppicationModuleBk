//package com.application.controller;
//
//
//
//import com.application.dto.DistributionRequestDTO;
//import com.application.service.DistributionToZoneService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.Map;
//
///**
// * REST Controller to handle application distribution requests.
// */
//@RestController
//@RequestMapping("/api/v1/application-distribution")
//@RequiredArgsConstructor
//public class DistributionToZoneController {
//
//    private final DistributionToZoneService distributionService;
//
//    /**
//     * Endpoint to create a new application distribution and its corresponding balance track.
//     * Receives form data as a JSON object which is mapped to the DistributionRequestDTO.
//     *
//     * @param request The DTO containing validated data from the frontend form.
//     * @return A ResponseEntity indicating success or failure.
//     */
//    @PostMapping("/create")
//    public ResponseEntity<Map<String, String>> createDistribution(@Valid @RequestBody DistributionRequestDTO request) {
//        try {
//            // Delegate all business logic to the service layer
//            distributionService.createDistributionAndBalance(request);
//            
//            // Create a simple success response message
//            Map<String, String> response = Collections.singletonMap("message", "Distribution created successfully.");
//            
//            // Return HTTP 201 Created status on success
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//        } catch (EntityNotFoundException e) {
//            // Handle cases where a related entity (like Employee or State) was not found
//            Map<String, String> errorResponse = Collections.singletonMap("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//
//        } catch (Exception e) {
//            // Catch any other unexpected errors during the process
//            Map<String, String> errorResponse = Collections.singletonMap("error", "An internal server error occurred: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//}
