package io.swagger.repository;

import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<dbUser, Long>{
    dbUser findUserByUsername(String username);
    dbUser getTransactionLimitByUsername(String username);
    @Transactional
    @Modifying
    @Query("update dbUser u set u.transactionLimit = ?1")
    void updateTransactionLimit(Double limit);
    //dbUser deletedbUserBy(Long id);
}

