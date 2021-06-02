package io.swagger.repository;

import io.swagger.model.dbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<dbUser, Long>{
    dbUser findUserByUsername(String username);


}
