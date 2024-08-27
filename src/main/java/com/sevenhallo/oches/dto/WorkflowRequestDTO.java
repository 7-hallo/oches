package com.sevenhallo.oches.dto;

import lombok.Data;

@Data
public class WorkflowRequestDTO {
    private String serviceName;
    private Object requestData;
}
