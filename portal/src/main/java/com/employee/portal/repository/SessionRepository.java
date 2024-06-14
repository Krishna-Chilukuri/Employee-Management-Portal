package com.employee.portal.repository;

import com.employee.portal.model.LoginSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccess, String> {
}
