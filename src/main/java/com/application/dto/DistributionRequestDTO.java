package com.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DistributionRequestDTO {

    @NotNull(message = "Academic Year ID is required")
    private Integer academicYearId;

    @NotNull(message = "State ID is required")
    private Integer stateId;

    @NotNull(message = "City ID is required")
    private Integer cityId;

    @NotNull(message = "Zone ID is required")
    private Integer zoneId;

    // Kept districtId as it is required by the database
    @NotNull(message = "District ID is required")
    private Integer districtId;

    // Removed campusId as the column does not exist in the database
    // private Integer campusId;

    @NotNull(message = "Issued By Type ID is required")
    private Integer issuedByTypeId;

    @NotNull(message = "Issued To Type ID is required")
    private Integer issuedToTypeId;

    @NotNull(message = "Issued To Employee ID is required")
    private Integer issuedToEmpId;

    @NotEmpty(message = "Application 'From' number is required")
    private String applicationNoFrom;

    @NotNull(message = "Range is required")
    @Min(value = 1, message = "Range must be at least 1")
    private Integer range;

    private Float amount;
    private LocalDate issueDate;
}
