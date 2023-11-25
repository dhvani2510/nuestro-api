package com.example.nuestro.controllers;
import com.example.nuestro.entities.Post;
import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.models.like.LikeRequest;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.services.PostService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/posts")
public class PostController {

    private  final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<ResponseModel> GetPosts()
    {
        var posts=  postService.GetPosts();
        return ResponseModel.Ok("Posts fetched", posts);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseModel> GetPost(@PathVariable String id)
    {
        try{
            var post= postService.GetPost(id);
            return ResponseModel.Ok("Post fetched", post);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseModel> CreatePost(@RequestBody PostRequest postRequest)
    {
        try{
            var post= postService.CreatePost(postRequest);
            return ResponseModel.Ok("Post created successfully", post);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseModel> UpdatePost(@RequestBody PostRequest postRequest, @PathVariable String id)
    {
        try{
            var post= postService.UpdatePost(id,postRequest);
            return ResponseModel.Ok("Post updated successfully", post);
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
            postService.Delete(id);
            return ResponseModel.Ok("Post deleted successfully", id);
        }
        catch (NuestroException e){
            return  ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/like")
    public ResponseEntity<ResponseModel> likePost(@RequestBody LikeRequest like) {
        try{
            Post post = postService.updateLike(like.getId());
            return ResponseModel.Ok("Post updated",post);
        }
        catch (NuestroException e){
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
