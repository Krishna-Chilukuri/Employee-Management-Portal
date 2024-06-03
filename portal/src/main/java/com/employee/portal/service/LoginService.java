package com.employee.portal.service;

import com.employee.portal.model.Login;

import java.util.List;

public interface LoginService {
    Login getLoginById(String loginId);
    List<Login> findAll();
}
