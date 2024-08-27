package com.sevenhallo.oches.dto;

import lombok.Data;

@Data
public class OchesRequestDTO {
    private String id;
    private int stepNo;
    private Object requestData;
    private boolean isCompleted;

    public OchesRequestDTO(Object requestData, String id, int stepNo) {
        this.requestData = requestData;
        this.id = id;
        this.stepNo = stepNo;
    }
}
