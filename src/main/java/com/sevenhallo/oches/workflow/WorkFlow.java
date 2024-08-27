package com.sevenhallo.oches.workflow;

import com.sevenhallo.oches.context.OchesContext;
import com.sevenhallo.oches.dto.OchesRequestDTO;
import com.sevenhallo.oches.dto.ResToStatusDTO;
import lombok.Getter;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

public class WorkFlow {
    @Getter
    private final String id;
    private final List<Step> steps = new ArrayList<>();

    public WorkFlow() {
        this.id = OchesContext.getUniqueId();
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public void startChainRequest(KafkaTemplate<String, OchesRequestDTO> kafkaTemplate) {
        for (Step step : steps) {
            step.makeProcessRequest(kafkaTemplate);
        }
    }

    public void afterProcessed(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate) {
        if(shouldTriggerChainRollback()) {
            triggerChainRollback(kafkaTemplate);
        } else {
            makeResponseToStatus(kafkaTemplate);
        }
    }

    public Step getStep(int stepNo) {
        return steps.get(stepNo);
    }

    public boolean ifWorkflowFinished() {
        return steps.stream().anyMatch(step -> step.getRequestDTO().isCompleted());
    }

    private void makeResponseToStatus(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate) {
        for (Step step : steps) {
            step.assessStatusAndMakeResponse(kafkaTemplate);
        }
    }

    private boolean shouldTriggerChainRollback() {
        return steps.stream().anyMatch(step -> step.getResponseDTO().isTriggerRollback());
    }

    private void triggerChainRollback(KafkaTemplate<String, ResToStatusDTO> kafkaTemplate) {
        for (Step step : steps) {
            step.triggerRollback(kafkaTemplate);
        }
    }
}
