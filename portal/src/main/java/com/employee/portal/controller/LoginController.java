package com.employee.portal.controller;

import com.employee.portal.factory.Logger;
import com.employee.portal.model.Login;
import com.employee.portal.model.LoginSuccess;
import com.employee.portal.service.implementation.LoginServiceImplementation;
import com.employee.portal.service.implementation.LoginSuccessImplementation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.apache.juli.logging.Log;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final LoginServiceImplementation loginServiceImplementation;
    private final LoginSuccessImplementation loginSuccessImplementation;

    public LoginController(LoginServiceImplementation loginServiceImplementation, LoginSuccessImplementation loginSuccessImplementation) {
        this.loginServiceImplementation = loginServiceImplementation;
        this.loginSuccessImplementation = loginSuccessImplementation;
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
    public LoginSuccess getLogin(@RequestParam(name = "id") String username, @RequestParam(name = "password") String password) throws IOException {
        Login login = loginServiceImplementation.getLoginById(username);
        if (login.getUsername() == null) {
            return new LoginSuccess();
        }
        Logger lg = Logger.getInstance();
        lg.log("Login Attempted");
        if (login.getPassword().equals(password)) {
//            Cookie
//            Logger lg= Logger.getInstance();
//            Cookie ck = new Cookie("sessionId", "kp");
//            response.addCookie(ck);

            LoginSuccess ls = new LoginSuccess();

            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            ls.setUsername(username);
            ls.setSessionId(sessionId);
            loginSuccessImplementation.saveLoginSuccess(ls);

            lg.log(username + " login successful!");
            return ls;
//            return Response.ok().entity(    )
        }
        else {
            lg.log("Login Failed");
            return new LoginSuccess();
        }
    }

    @RequestMapping("/promoteToOwner")
    void promoteAdminToOwner(@RequestParam(name = "adminId") String adminId) throws IOException {
        Logger lg = Logger.getInstance();
        Login log = loginServiceImplementation.getLoginById(adminId);
        if (log.getUsername() == null) {
            lg.log("User not Found in Login");
            return;
        } else if (Objects.equals(log.getPrivilege(), "guest") || Objects.equals(log.getPrivilege(), "priv_user")) {
            lg.log("User is not an Admin to be promoted");
            return;
        } else if (Objects.equals(log.getPrivilege(), "owner")) {
            lg.log("User is already an Owner");
            return;
        }
        lg.log("User " + adminId + " is being promoted to Owner");
        loginServiceImplementation.updatePrivilege(adminId, "owner");
    }

    @RequestMapping("/demoteOwner")
    void demoteOwnerToAdmin(@RequestParam(name = "ownerId") String ownerId) throws IOException {
        Logger lg = Logger.getInstance();
        Login log = loginServiceImplementation.getLoginById(ownerId);
        if (log.getUsername() == null) {
            lg.log("User not Found in Login");
            return;
        } else if (Objects.equals(log.getPrivilege(), "guest") || Objects.equals(log.getPrivilege(), "priv_user")) {
            lg.log("User is not an Admin to be promoted");
            return;
        } else if (Objects.equals(log.getPrivilege(), "admin")) {
            lg.log("User is already an Admin");
            return;
        }
        lg.log("User " + ownerId + " is demoted to Admin");
        loginServiceImplementation.updatePrivilege(ownerId, "admin");
    }

    @RequestMapping("/removeAdminOwner")
    void removeAdminOwner(@RequestParam(name = "username") String username) throws IOException {
        Logger lg = Logger.getInstance();
        Login log = loginServiceImplementation.getLoginById(username);
        if (log.getUsername() == null) {
            lg.log("User not Found in Login");
            return;
        } else if (Objects.equals(log.getPrivilege(), "guest") || Objects.equals(log.getPrivilege(), "priv_user")) {
            lg.log("User is not an Admin / Owner");
            return;
        }
        lg.log("User " + username + " is removed");
        loginServiceImplementation.removeLogin(username);
    }


    @RequestMapping("/getAll")
    public List<Login> getAllLogins() {
        return loginServiceImplementation.loginRepository.findAll();
    }
}
