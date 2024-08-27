package com.sevenhallo.oches.config;

import com.sevenhallo.oches.dto.OchesResponseDTO;
import com.sevenhallo.oches.dto.WorkflowRequestDTO;
import com.sevenhallo.oches.service.OchesService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class KafkaFunction {

    private final OchesService ochesService;

    @Bean
    public Consumer<List<WorkflowRequestDTO>> startWorkFlow() {
        return ochesService::startWorkflow;
    }

    @Bean
    public Consumer<OchesResponseDTO> stepResponseProcessor() {
        return ochesService::processStepResponse;
    }
}
