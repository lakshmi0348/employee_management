package com.wcs.employee_management.employee_management.repository;

import com.wcs.employee_management.employee_management.entity.Role;
import com.wcs.employee_management.employee_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    List<User> findByIsUserActive(boolean isUserActive);

}
