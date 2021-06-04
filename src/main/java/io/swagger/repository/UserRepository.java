package io.swagger.repository;

import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<dbUser, Long>{
    dbUser findUserByUsername(String username);

    dbUser findUserById(Long id);



}
