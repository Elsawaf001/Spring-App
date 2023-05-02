package com.elsawaf.supportportal.repository;

import com.elsawaf.supportportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String userName);
    User findUserByEmail(String email);

    User findByUserNameLikeIgnoreCaseAllIgnoreCase(@NonNull String userName);

}