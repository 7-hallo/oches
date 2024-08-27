package com.sevenhallo.oches.service;

import com.sevenhallo.oches.context.OchesContext;
import com.sevenhallo.oches.dto.OchesRequestDTO;
import com.sevenhallo.oches.dto.OchesResponseDTO;
import com.sevenhallo.oches.dto.ResToStatusDTO;
import com.sevenhallo.oches.dto.WorkflowRequestDTO;
import com.sevenhallo.oches.workflow.Step;
import com.sevenhallo.oches.workflow.WorkFlow;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OchesService {

    private final KafkaTemplate<String, OchesRequestDTO> ochesReqPublisher;
    private final KafkaTemplate<String, ResToStatusDTO> resToStatusPublisher;

    public void startWorkflow(List<WorkflowRequestDTO> workflowRequests) {
        WorkFlow workFlow = new WorkFlow();
        for (int i = 0; i < workflowRequests.size(); i++) {
            Step step = new Step(
                    workflowRequests.get(i),
                    i,
                    workFlow.getId()
            );
            workFlow.addStep(step);
        }
        OchesContext.addWorkflow(workFlow);
        workFlow.startChainRequest(ochesReqPublisher);
    }

    public void processStepResponse(OchesResponseDTO responseDTO) {
        WorkFlow workFlow = OchesContext.getWorkflow(responseDTO.getId());
        Step step = workFlow.getStep(responseDTO.getStep());
        step.getRequestDTO().setCompleted(true);
        step.setResponseDTO(responseDTO);
        if(workFlow.ifWorkflowFinished()) {
            workFlow.afterProcessed(resToStatusPublisher);
            OchesContext.removeWorkflow(workFlow.getId());
        }
    }
}
