package com.elsawaf.app.jwtAuth.enumeration;


import static com.elsawaf.app.jwtAuth.constant.Authority.*;

public enum Role {


    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES) ,
    ROLE_MANAGER(MANAGER_AUTHORITIES) ,
    ROLE_ADMIN(ADMIN_AUTHORITIES) ,
    ROLE_SUPER_USER(SUPER_USER_AUTHORITIES);

    private String[] authorities;

    public String[] getAuthorities() {
        return authorities;
    }

    Role(String... authorities) {
        this.authorities=authorities;
    }
}
