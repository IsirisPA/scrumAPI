package gr.vgs.mongo.service.impl;

import gr.vgs.mongo.entity.User;
import gr.vgs.mongo.enums.ETaskStatus;
import gr.vgs.mongo.entity.Task;
import gr.vgs.mongo.model.TaskModel;
import gr.vgs.mongo.model.TaskResponse;
import gr.vgs.mongo.repository.TaskRepository;
import gr.vgs.mongo.repository.UserRepository;
import gr.vgs.mongo.service.SprintService;
import gr.vgs.mongo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SprintService sprintService;

    @Override
    public String addTask(TaskModel taskModel, String username) {
        try {
            Task task = new Task();
            Date date = new Date();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            task.setCreatorUserId(user.getId());
            task.setTitle(taskModel.getTitle());
            task.setAssignedUserId(taskModel.getAssignedUserId());
            task.setCreateDate(date);
            task.setTaskType(taskModel.getTaskType());
            task.setDescription(taskModel.getDescription());
            task.seteTaskStatus(ETaskStatus.PENDING);
            task.setSprintId(taskModel.getSprintId());

            //need to update sprint tasks
            taskRepository.save(task);
            sprintService.editTaskToSprint(task.getSprintId(), task, "add");
            return "Task added successfully";
        } catch (Exception e) {
            return "Task add failed";
        }

    }

    @Override
    public String removeTask(String taskId) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Error Task not found"));
            taskRepository.delete(task);
            sprintService.editTaskToSprint(task.getSprintId(), task, "delete");
            return "Task removed successfully";
        } catch (Exception e) {
            return "Task remove failed";
        }
    }

    @Override
    public TaskModel getTask(String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Error Task not found"));
        TaskModel taskModel = new TaskModel();
        taskModel.setId(task.getId());
        taskModel.setSprintId(task.getSprintId());
        taskModel.setDescription(task.getDescription());
        taskModel.setTitle(task.getTitle());
        taskModel.setTaskType(task.getTaskType());
        taskModel.setAssignedUserId(task.getAssignedUserId());
        taskModel.setCreatorUserId(task.getCreatorUserId());
        taskModel.setStatus(task.geteTaskStatus());
        if (task.getCompletedDate() != null) taskModel.setCompletedDate(task.getCompletedDate());

        return taskModel;
    }

    @Override
    public String changeTaskStatus(String taskId, String status) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
            switch (status) {
                case "pending":
                    task.seteTaskStatus(ETaskStatus.PENDING);
                    break;
                case "develop":
                    task.seteTaskStatus(ETaskStatus.IN_DEVELOPMENT);
                    break;
                case "completed":
                    task.seteTaskStatus(ETaskStatus.COMPLETED);
                    break;
            }
            taskRepository.save(task);
            return "Task status changed successfully";
        } catch (Exception e) {
            return "Task status change failed";
        }

    }

    @Override
    public String assignUserTask(String taskId, String userId) {
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
            task.setAssignedUserId(userId);
            taskRepository.save(task);
            return "Task was assigned successfully";
        } catch (Exception e) {
            return "Task assign failed";
        }
    }

    @Override
    public String editTask(TaskModel taskModel) {
        try {
            Task task = taskRepository.findById(taskModel.getId()).orElseThrow(() -> new RuntimeException("Error Task not found"));
            if (taskModel.getTitle() != null) task.setTitle(taskModel.getTitle());
            if (taskModel.getAssignedUserId() != null) task.setAssignedUserId(taskModel.getAssignedUserId());
            if (taskModel.getCreatorUserId() != null) task.setCreatorUserId(taskModel.getCreatorUserId());
            if (taskModel.getCompletedDate() != null) task.setCompletedDate(taskModel.getCompletedDate());
            if (taskModel.getDescription() != null) task.setDescription(taskModel.getDescription());
            if (taskModel.getStatus() != null) task.seteTaskStatus(ETaskStatus.PENDING);
            if (taskModel.getSprintId() != null) task.setSprintId(taskModel.getSprintId());
            taskRepository.save(task);
            return "Task edited successfully";
        } catch (Exception e) {
            return "Task edit failed";
        }

    }

    @Override
    public List<TaskResponse> getUserTasks(String username) {
        System.out.println("username is " +username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        List<Task> tasks = taskRepository.findAllByAssignedUserId(user.getId());
        List<TaskResponse> responses = new ArrayList<>();
        TaskResponse taskResponse;
        if (tasks != null) {
            for (Task task:tasks) {
                taskResponse = new TaskResponse();
                taskResponse.setTaskId(task.getId());
                taskResponse.setTitle(task.getTitle());
                responses.add(taskResponse);
            }
        }
        return responses;
    }
}
