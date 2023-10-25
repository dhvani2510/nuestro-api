package com.example.nuestro.controllers;

import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.services.FileService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

//@RestController
//@RequestMapping(path="api/v1/files")
public class FileController {

    private  final FileService fileService;
    private static final Logger logger= LoggerFactory.getLogger(FileController.class);
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Resource> Get(@PathVariable String id) //TO Download replace inline with attachment
    {
        try{

            var file= fileService.Get(id);

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getBytes()));

            var contentType=MediaType.parseMediaType(file.getContentType());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .contentType( contentType)
                    //.contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        catch (NuestroException e){
            logger.error(e.getMessage(),e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ResponseEntity("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseModel> Update(@PathVariable String id, @ModelAttribute MultipartFile file)
    {
        try{
            var result= fileService.Update(id, file);
            return ResponseModel.Ok("File updated successfully", result);
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

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseModel> Upload(@ModelAttribute MultipartFile file)
    {
        try{
            var result= fileService.UploadFile(file);
            return ResponseModel.Ok("File uploaded successfully", result);
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
}
