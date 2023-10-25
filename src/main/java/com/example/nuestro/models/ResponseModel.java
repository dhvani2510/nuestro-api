package com.example.nuestro.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseModel
{
    private  int status;
    private String message;
    private Object data;

    public ResponseModel(int status,String message, Object data) {
        this.status=status;
        this.message = message;
        this.data = data;
    }

    public ResponseModel(int status, String message) {
        this.status= status;
        this.message = message;
    }
    public  static ResponseEntity<ResponseModel> Ok(String message){
        var status=HttpStatus.OK;
        var response= new ResponseModel(status.value(), message,null);
        return new ResponseEntity(response, status);
    }
    public  static ResponseEntity<ResponseModel> Ok(String message, Object data){
        var status=HttpStatus.OK;
        var response= new ResponseModel(status.value(), message,data);
        return new ResponseEntity(response, status);
    }

    public static ResponseEntity<ResponseModel> Fail(String message,  HttpStatus status){
        if(status==HttpStatus.OK)
            return null;
        var response= new ResponseModel(status.value(), message,null);
        return new ResponseEntity(response, status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
