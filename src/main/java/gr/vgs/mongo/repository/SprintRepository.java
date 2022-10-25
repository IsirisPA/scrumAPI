package gr.vgs.mongo.repository;

import gr.vgs.mongo.entity.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SprintRepository extends MongoRepository<Sprint, String > {
    List<Sprint> findAllByProjectId(String id);
}
