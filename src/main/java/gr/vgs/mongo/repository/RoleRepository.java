package gr.vgs.mongo.repository;

import gr.vgs.mongo.enums.ERole;
import gr.vgs.mongo.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
