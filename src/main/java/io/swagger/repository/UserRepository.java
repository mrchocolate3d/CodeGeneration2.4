package io.swagger.repository;

import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<dbUser, Long>{
    dbUser findUserByUsername(String username);
    //dbUser deletedbUserBy(Long id);

    //@Query("SELECT * FROM dbUser WHERE NOT (Id = 1)")
    //dbUser getAllUsersExceptForBank();
}

