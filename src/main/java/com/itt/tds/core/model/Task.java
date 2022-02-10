package com.itt.tds.core.model;

import com.itt.tds.core.enums.TaskStatus;
import java.io.Serializable;

public class Task implements Serializable {

    private String id;
    private String clientId;
    private String nodeId;
    private TaskStatus status;
    private TaskResult result;
    private String filePath;
    private String data;
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Task() {
        id = "";
        clientId = "";
        nodeId = "";
        status = TaskStatus.PENDING;
        result = new TaskResult();
        filePath = "";
        data = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskResult getResult() {
        return result;
    }

    public void setResult(TaskResult result) {
        this.result = result;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
