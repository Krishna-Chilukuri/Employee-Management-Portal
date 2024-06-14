package com.employee.portal.service;

import com.employee.portal.model.LoginSuccess;

public interface LoginSuccessService {
    LoginSuccess getLoginSuccessById(String sessionId);
    LoginSuccess saveLoginSuccess(LoginSuccess logSS);
}
