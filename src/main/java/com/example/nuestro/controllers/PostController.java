package com.example.nuestro.controllers;
import com.example.nuestro.entities.Post;
import com.example.nuestro.models.ResponseModel;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.models.post.SearchPostRequest;
import com.example.nuestro.services.PostService;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/posts")
public class PostController {

    private  final PostService postService;
    private static final Logger logger= LoggerFactory.getLogger(PostController.class);
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

    @GetMapping("users/{userId}")
    public ResponseEntity<ResponseModel> GetPosts(@PathVariable String userId)
    {
        var posts=  postService.GetPosts(userId);
        return ResponseModel.Ok("User posts fetched", posts);
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
            logger.error(e.getMessage(), e.getCause());
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

    @PostMapping("{id}/like")
    public ResponseEntity<ResponseModel> likePost(@PathVariable String id) {
        try {
            var like = postService.LikePost(id);
            return   ResponseModel.Ok(like ==null?  "Post unliked":"Post liked", like);
        } catch (NuestroException e) {
            return ResponseModel.Fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("search")
    public ResponseEntity<ResponseModel> SearchPosts(@RequestParam(name = "keyword", required = false) String keyword )  // @RequestParam(name = "userId", required = false) String userId
    //@ModelAttribute SearchPostRequest request
    {
        var request = new SearchPostRequest(keyword);
        try{
            var posts= postService.SearchPosts(request);
            return ResponseModel.Ok("Posts fetched", posts);
        } catch (Exception e){
            return  ResponseModel.Fail("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
