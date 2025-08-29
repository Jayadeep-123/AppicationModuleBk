//package com.application.controller;
//
//import com.application.dto.NextAppNoResponseDTO;
//import com.application.service.DistributionToZoneService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/form-data")
//@RequiredArgsConstructor
//public class FormDataController {
//
//    private final DistributionToZoneService distributionToZoneService;
//
//    /**
//     * Endpoint for the frontend to fetch the next available application number
//     * based on the selected state and academic year.
//     *
//     * @param stateId The ID of the state selected in the dropdown.
//     * @param academicYearId The ID of the academic year selected.
//     * @return A DTO containing the formatted next application number string.
//     */
//    @GetMapping("/next-app-no/{stateId}/{academicYearId}")
//    public ResponseEntity<NextAppNoResponseDTO> getNextApplicationNumber(
//            @PathVariable Integer stateId,
//            @PathVariable Integer academicYearId) {
//        
//        try {
//            // This method calculates the next available application number string.
//            String nextAppNo = distributionToZoneService.calculateNextApplicationNo(stateId, academicYearId);
//            
//            // On success, return the correct DTO with the calculated number.
//            return ResponseEntity.ok(new NextAppNoResponseDTO(nextAppNo));
//
//        } catch (Exception e) {
//            // On failure, return the same DTO with an error message.
//            return ResponseEntity.badRequest().body(new NextAppNoResponseDTO("Error: " + e.getMessage()));
//        }
//    }
//}
