package com.employee.portal.controller;

import com.employee.portal.model.Login;
import com.employee.portal.repository.LoginRepository;
import com.employee.portal.service.implementation.LoginServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final LoginServiceImplementation loginServiceImplementation;

    public LoginController(LoginServiceImplementation loginServiceImplementation) {
        this.loginServiceImplementation = loginServiceImplementation;
    }

//    @RequestMapping("/findId")
//    public Login getLogin(@RequestParam(name = "id") String username, @RequestParam(name = "password") String password) {
//        Login res = loginServiceImplementation.getLoginById(username);
//        if (res.getPassword().equals(password)) {
//            return res;
//        }
//        else {
//            return null;
//        }
//    }


    @GetMapping(value = "/getId", produces = "application/json")
    public Login getLogin(@RequestParam(name = "id") String username, @RequestParam(name = "password") String password) {
        Login log = loginServiceImplementation.getLoginById(username);
        if (log.getPassword().equals(password)) {
            return log;
        }
        else {
            return new Login();
        }
    }


    @RequestMapping("/getAll")
    public List<Login> getAllLogins() {
        return loginServiceImplementation.loginRepository.findAll();
    }
}
