package com.sevenhallo.oches.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OchesResponseDTO {
    private String id;
    private int step;
    private String status;
    private Map<String, String> responseToStatusMapping;
    private Object backupData;
    private Object currentData;
    private String tableName;
    private boolean triggerRollback;
}
