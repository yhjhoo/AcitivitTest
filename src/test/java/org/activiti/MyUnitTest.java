package org.activiti;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import org.activiti.spring.SpringProcessEngineConfiguration;;


public class MyUnitTest {
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	DelegateTask task;;
	
	@Test
	@Deployment(resources = {"org/activiti/test/my-process.bpmn"})
	public void test() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		assertNotNull(processInstance);
		
		Task task = taskService.createTaskQuery().singleResult();
		taskService.complete(task.getId() );
		
		assertEquals("Activiti is awesome!!", task.getName());
		
		
		System.out.println("\n1----------------------------------");
		for(Task tmp_tsk : taskService.createTaskQuery().list()){
			System.out.println(tmp_tsk.getName());
			if("alfrescoUsertask1".equals(tmp_tsk.getTaskDefinitionKey() )){
				taskService.complete(tmp_tsk.getId() );
			}
		}
		
		System.out.println("\n2----------------------------------");
		for(Task tmp_tsk : taskService.createTaskQuery().list()){
			System.out.println(tmp_tsk.getName());
			
			if("task1".equals(tmp_tsk.getTaskDefinitionKey() )){
				taskService.complete(tmp_tsk.getId() );
			}
		}
		
		
//		System.out.println("\n3----------------------------------");
//		Execution execution = runtimeService.createExecutionQuery()
//											.processInstanceId(processInstance.getId())
//											.activityId("Reject")
//											.singleResult();
//		assertNotNull(execution);
//		runtimeService.signal(execution.getId());
		
		System.out.println("\n4----------------------------------");
		for(Task tmp_tsk : taskService.createTaskQuery().list()){
			System.out.println(tmp_tsk.getName());
		}
	}
	
	
	@Test
	@Deployment(resources = {"org/activiti/test/ReceiveTaskTest.testWaitStateBehavior.bpmn"})
	public void test1() {
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("receiveTask");
		assertNotNull(processInstance);
		
		Execution execution = runtimeService.createExecutionQuery()
			      .processInstanceId(processInstance.getId())
			      .activityId("waitState")
			      .singleResult();
		assertNotNull(execution);
		
		runtimeService.signal(execution.getId());
		System.out.println("\n1----------------------------------");
		for(Task tmp_tsk : taskService.createTaskQuery().list()){
			System.out.println(tmp_tsk.getName());
		}
		
	}

}
