package io.swagger.service;

import io.swagger.model.dbUser;

import java.util.List;

public interface UserService {
    dbUser addUser(dbUser user);
    dbUser getUserByUsername(String username);

    List<dbUser> getUsers();
}
