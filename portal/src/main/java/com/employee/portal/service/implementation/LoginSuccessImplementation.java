package com.employee.portal.service.implementation;

import com.employee.portal.model.LoginSuccess;
import com.employee.portal.repository.LoginSuccessRepository;
import com.employee.portal.service.LoginSuccessService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginSuccessImplementation implements LoginSuccessService {
    public LoginSuccessRepository loginSuccessRepository;

    public LoginSuccessImplementation(LoginSuccessRepository loginSuccessRepository) {
        super();
        this.loginSuccessRepository = loginSuccessRepository;
    }


    @Override
    public LoginSuccess getLoginSuccessById(String sessionId) {
        Optional<LoginSuccess> testSucc = loginSuccessRepository.findById(sessionId);
        return testSucc.orElseGet(LoginSuccess::new);
    }

    @Override
    public LoginSuccess saveLoginSuccess(LoginSuccess logSS) {
        return loginSuccessRepository.save(logSS);
    }
}
