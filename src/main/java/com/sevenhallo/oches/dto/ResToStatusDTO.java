package com.sevenhallo.oches.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResToStatusDTO {
    private String responseMessage;
    private Object backupData;
    private Object currentData;
    private String tableName;
}
