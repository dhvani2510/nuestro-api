package com.example.nuestro.controllers;

import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.models.auth.AuthenticationRequest;
import com.example.nuestro.models.auth.RegisterRequest;
import com.example.nuestro.services.UserService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")

public class AuthController {

    private  final UserService  userService; //authenticationService
    private static final Logger logger= LoggerFactory.getLogger(AuthController.class);
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<ResponseModel> Register(@RequestBody RegisterRequest registerRequest){

        try{
            var response=userService.register(registerRequest);
            return ResponseModel.Ok("User registered", response);
        }
        catch (NuestroException e){
            logger.error(e.getMessage());
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseModel.Fail("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("login")
    public ResponseEntity<ResponseModel> Authentication(@RequestBody AuthenticationRequest authenticationRequest)
    {
        try{
            var response=userService.authenticate(authenticationRequest);
            return ResponseModel.Ok("User authenticated", response);
        }
        catch (NuestroException e){
             logger.error(e.getMessage());
             return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseModel.Fail("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
