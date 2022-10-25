package gr.vgs.mongo.repository;

import gr.vgs.mongo.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findAllByAssignedUserId(String id);
}
