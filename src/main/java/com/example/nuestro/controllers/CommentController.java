package com.example.nuestro.controllers;

import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.models.comment.CommentRequest;
import com.example.nuestro.services.CommentService;
import com.example.nuestro.services.PostService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/comments")
public class CommentController {
    private  final CommentService commentService;
    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseModel> GetComment(@PathVariable String id)
    {
        try{
            var post= commentService.GetComment(id);
            return ResponseModel.Ok("Comment fetched", post);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("posts/{postId}")
    public ResponseEntity<ResponseModel> CreateComment(@RequestBody CommentRequest commentRequest, @PathVariable String postId)
    {
        try{
            var post= commentService.CreateComment(commentRequest, postId);
            return ResponseModel.Ok("Comment created successfully", post);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            var cause=  e.getCause();
            logger.error(cause==null? e.getMessage(): e.getMessage() + e.getCause().getMessage(),e);
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseModel> UpdateComment(@RequestBody CommentRequest commentRequest, @PathVariable String id)
    {
        try{
            var comment= commentService.UpdateComment(id,commentRequest);
            return ResponseModel.Ok("Comment updated successfully", comment);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseModel> Delete(@PathVariable String id)
    {
        try{
            commentService.Delete(id);
            return ResponseModel.Ok("Comment deleted successfully",id);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
