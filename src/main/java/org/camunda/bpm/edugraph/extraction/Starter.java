package org.camunda.bpm.edugraph.extraction;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class Starter implements InitializingBean {

  @Autowired
  private RuntimeService runtimeService;

  public void afterPropertiesSet() throws Exception {
    runtimeService.startProcessInstanceByKey("edu-graph");
  }

  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }
}