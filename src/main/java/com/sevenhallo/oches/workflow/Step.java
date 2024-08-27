package com.sevenhallo.oches.workflow;

import com.sevenhallo.oches.dto.OchesRequestDTO;
import com.sevenhallo.oches.dto.OchesResponseDTO;
import com.sevenhallo.oches.dto.ResToStatusDTO;
import com.sevenhallo.oches.dto.WorkflowRequestDTO;
import lombok.Data;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

@Data
public class Step {
    private final int stepNo;
    private final String name;
    private OchesRequestDTO requestDTO;
    private OchesResponseDTO responseDTO;

    public Step(WorkflowRequestDTO workflowRequest, int stepNo, String id) {
        this.name = workflowRequest.getServiceName();
        this.stepNo = stepNo;
        this.requestDTO = new OchesRequestDTO(workflowRequest.getRequestData(), id, stepNo);
    }

    @Async
    public void makeProcessRequest(KafkaTemplate<String, OchesRequestDTO> kafkaTemplate) {
        kafkaTemplate.send("oches-req-" + this.name, this.requestDTO);
    }

    public void triggerRollback(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate) {
        kafkaTemplate.send("oches-res-" + this.name, makeResToStatusDTO("revert"));
    }

    public void assessStatusAndMakeResponse(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate) {
        String status = responseDTO.getStatus();
        Map<String, String> responseToStatus =                             
                responseDTO.getResponseToStatusMapping();
        String responseMessage = responseToStatus.get(status);
        if(responseMessage != null && !responseMessage.isBlank()) {
            makeStatusResponse(kafkaTemplate, responseMessage);
        }
    }

    private void makeStatusResponse(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate, String message) {
        kafkaTemplate.send("oches-res-" + this.name, makeResToStatusDTO(message));
    }

    private ResToStatusDTO makeResToStatusDTO(String message) {
        return new ResToStatusDTO(
                message,
                this.getResponseDTO().getBackupData(),
                this.getResponseDTO().getCurrentData(),
                this.getResponseDTO().getTableName()
        );
    }

}
