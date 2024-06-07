package com.employee.portal.service.implementation;

import com.employee.portal.model.Login;
import com.employee.portal.repository.LoginRepository;
import com.employee.portal.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LoginServiceImplementation implements LoginService {
    public LoginRepository loginRepository;

    public LoginServiceImplementation(LoginRepository loginRepository) {
        super();
        this.loginRepository = loginRepository;
    }

    @Override
    public Login getLoginById(String loginId) {
        Optional<Login> testLog = loginRepository.findById(loginId);
        return testLog.orElseGet(Login::new);
    }

    @Override
    public List<Login> findAll() {
        return loginRepository.findAll();
    }

    @Override
    public Login saveLogin(Login log) {
        return loginRepository.save(log);
    }


}
