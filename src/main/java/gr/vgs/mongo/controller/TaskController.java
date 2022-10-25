package gr.vgs.mongo.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import gr.vgs.mongo.model.TaskModel;
import gr.vgs.mongo.model.TaskResponse;
import gr.vgs.mongo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskModel taskModel) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        String response = taskService.addTask(taskModel, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTask(@Valid @RequestBody ObjectNode node)  {
        String response = taskService.removeTask(node.get("taskId").asText(""));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<TaskModel> previewTask(@Valid @RequestBody ObjectNode node) {
        taskService.getTask(node.get("taskId").asText(""));
        return ResponseEntity.ok(taskService.getTask(node.get("taskId").asText("")));
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editTask(@Valid @RequestBody TaskModel taskModel) {
        String response = taskService.editTask(taskModel);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/user")
    public ResponseEntity<List<TaskResponse>> getUserTasks() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        taskService.getUserTasks(username);
        return ResponseEntity.ok(taskService.getUserTasks(username));
    }

    @PostMapping("/assign/user")
    public ResponseEntity<?> assignUserTask(@Valid @RequestBody ObjectNode node) {
        String response = taskService.assignUserTask(node.get("taskId").asText(""), node.get("userId").asText(""));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<?> changeTaskStatus(@Valid @RequestBody ObjectNode node) {
        String response = taskService.changeTaskStatus(node.get("taskId").asText(""), node.get("status").asText(""));
        return ResponseEntity.ok(response);
    }
}
