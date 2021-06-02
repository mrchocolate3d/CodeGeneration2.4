package io.swagger.model;

import org.springframework.security.core.GrantedAuthority;

public enum AccountType implements GrantedAuthority {
    TYPE_SAVING,
    TYPE_CURRENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
