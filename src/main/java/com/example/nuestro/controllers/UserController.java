package com.example.nuestro.controllers;

import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.models.users.ProfileRequest;
import com.example.nuestro.services.UserService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/users")
public class UserController {
    private  final  UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    public  UserController(UserService userService){
        this.userService= userService;
    }

    @GetMapping
    public ResponseEntity<ResponseModel> GetUsers()
    {
        var users=  userService.GetUsers();
        return ResponseModel.Ok("Users fetched", users);
    }

    @GetMapping("me")
    public ResponseEntity<ResponseModel> GetMyProfile()
    {
        try{
            var user= userService.GetUser();
            return ResponseModel.Ok("My user profile fetched", user);
        }
        catch (NuestroException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            path = "profile",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseModel> UpdateProfile(@ModelAttribute ProfileRequest profileRequest)
    {
        try{
            var user= userService.UpdateProfile(profileRequest);
            return ResponseModel.Ok("Profile updated successfully", user);
        }
        catch (NuestroException e){
            logger.error(e.getMessage(),e);
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseModel> GetProfile( @PathVariable String id) //@RequestParam Long id
    {
        try{
            var user= userService.GetProfile(id);
            return ResponseModel.Ok("User profile fetched", user);
        }
        catch (NuestroException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
