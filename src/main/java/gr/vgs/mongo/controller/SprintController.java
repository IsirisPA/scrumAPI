package gr.vgs.mongo.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import gr.vgs.mongo.model.SprintRequest;
import gr.vgs.mongo.model.SprintResponse;
import gr.vgs.mongo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sprint")
public class SprintController {
    @Autowired
    SprintService sprintService;


    @PostMapping("/add")
    public ResponseEntity<?> addSprint(@Valid @RequestBody SprintRequest sprintRequest) {
        String response = sprintService.addSprint(sprintRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<SprintRequest> previewSprint(@Valid @RequestBody ObjectNode node) {
        SprintRequest sprintRequest = sprintService.getSprint(node.get("sprintId").asText(""));
        return ResponseEntity.ok(sprintRequest);
    }

    @GetMapping("/get/project")
    public ResponseEntity<List<SprintResponse>> previewProjectSprints(@Valid @RequestBody ObjectNode node) {
        List<SprintResponse> list = sprintService.getProjectSprints(node.get("projectId").asText());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editSprint(@Valid @RequestBody SprintRequest sprintRequest) {
         String response = sprintService.editSprint(sprintRequest);
        return ResponseEntity.ok(response);
    }
}
