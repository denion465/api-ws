package com.api.ws.ui.model.response;

public class OperationStatusModel {
  private String operationResult;
  private String operationName;

  public String getOperationResult() {
    return this.operationResult;
  }

  public void setOperationResult(String operationResult) {
    this.operationResult = operationResult;
  }

  public String getOperationName() {
    return this.operationName;
  }

  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }
}
