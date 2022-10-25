package gr.vgs.mongo.service;

import gr.vgs.mongo.enums.ETaskStatus;
import gr.vgs.mongo.model.TaskModel;
import gr.vgs.mongo.model.TaskResponse;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    String addTask(TaskModel taskModel, String username);
    String editTask(TaskModel taskModel);
    String changeTaskStatus(String taskId, String status);
    TaskModel getTask(String taskId);
    List<TaskResponse> getUserTasks(String userId);
    String removeTask(String taskId);
    String assignUserTask(String taskId, String userId);
}
