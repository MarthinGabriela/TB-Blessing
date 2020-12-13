package systemapp.tbblessing.restcontroller;

import systemapp.tbblessing.model.*;
import systemapp.tbblessing.service.*;
import systemapp.tbblessing.object.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.json.*;

@RestController
@RequestMapping("api/v1")
public class UserRestController {
    @Autowired
    private LoginService service;
    
    @GetMapping("/login")
    private LoginModel handleLogin(
        @Valid @RequestBody LoginModel login
    ) {
        try {
            LoginModel log = service.getLoginByToken(login.getToken()).get();
            return log;
        } catch(NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Token Found!"
            );
        }
    }
}