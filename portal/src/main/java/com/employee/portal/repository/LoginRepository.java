package com.employee.portal.repository;

import com.employee.portal.model.Login;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<Login, String> {
    @Transactional
    @Modifying
    @Query("UPDATE Login SET privilege = :newPrivilege WHERE username = :username")
    void updatePrivilegeUsingId(String newPrivilege, String username);
}
