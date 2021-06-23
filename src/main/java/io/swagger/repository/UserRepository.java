package io.swagger.repository;

import io.swagger.model.InsertUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<InsertUser, Long>{
    InsertUser findUserByUsername(String username);

    //@Query("SELECT u FROM users u")
    //List<InsertUser> getUsers();
    //dbUser findUserById(Long id);
    //dbUser deletedbUserBy(Long id);
}

