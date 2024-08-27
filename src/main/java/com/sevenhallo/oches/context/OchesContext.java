package com.sevenhallo.oches.context;

import com.sevenhallo.oches.workflow.WorkFlow;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OchesContext {

    public static Map<String, WorkFlow> workflowContainer = new ConcurrentHashMap<>();

    public static String getUniqueId() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (workflowContainer.containsKey(id));
        return id;
    }

    public static void addWorkflow(WorkFlow workflow) {
        workflowContainer.put(workflow.getId(), workflow);
    }

    public static WorkFlow getWorkflow(String id) {
        return workflowContainer.get(id);
    }

    public static void removeWorkflow(String id) {
        workflowContainer.remove(id);
    }
}
