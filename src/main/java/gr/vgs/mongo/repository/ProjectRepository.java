package gr.vgs.mongo.repository;


import gr.vgs.mongo.entity.Project;
import gr.vgs.mongo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProjectRepository  extends MongoRepository<Project, String> {
    @Query("{'users' :{'$ref' : 'users' , '$_id' : ?0}}")
    List<Project> findProjectsByUsers(String id);
}
