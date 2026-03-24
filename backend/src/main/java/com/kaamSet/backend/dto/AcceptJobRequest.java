package com.kaamSet.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptJobRequest {

    private Double oneTimePrice;

    private Double perDaySalary;
}