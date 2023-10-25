package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.models.post.PostResponse;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService
{
    private  final PostRepository postRepository;
    private  final  UserService userService;

    private static final Logger logger= LoggerFactory.getLogger(PostService.class);
    @Autowired
    public PostService(PostRepository postRepository, UserService userService){

        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostResponse> GetPosts(){
        List<Post> posts= postRepository.findAll();
        List<PostResponse> result = posts.stream()
                .map(u -> new PostResponse(u))
                .toList();
        return result;
    }

    public  PostResponse GetPost(String id) throws NuestroException {
        logger.info("Getting post {}", id);
        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not found"));
        return  new PostResponse(post);
    }

    public  PostResponse CreatePost(PostRequest postRequest) throws NuestroException {
        logger.info("User adding post with content {}"
                , postRequest.getContent());

        ValidatePostRequest(postRequest);

//        var post= postRepository.findByEnglishAndHindiAndFrench(postRequest.getEnglish(),postRequest.getHindi(),postRequest.getFrench());
//        if(post!=null){
//            logger.error("Post already exists");
//            throw  new NuestroException("Post already exists");
//        }

        var user= userService.GetUserContext();
        if(user==null){
            logger.error("User not found");
            throw  new NuestroException("User not found");
        }
        var post= new Post(postRequest.getContent(),user);

        postRepository.save(post);
        return  new PostResponse(post);
    }

    public  PostResponse UpdatePost(String id, PostRequest postRequest) throws NuestroException {
        logger.info("User updating post {} with content {}"
            ,id, postRequest.getContent());

        ValidatePostRequest(postRequest);

        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));

        post.Set(postRequest);
        postRepository.save(post);
        return new PostResponse(post);
    }

    public  void Delete(String id) throws NuestroException {
        logger.info("User deleting post {}",id);
        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));
        //post.setDeletedAt(LocalDateTime.now()) ; postRepository.save(post);
        postRepository.delete(post);
        logger.info("Post deleted successfully");
    }

    private  void ValidatePostRequest(PostRequest postRequest) throws NuestroException {
        if(StringHelper.StringIsNullOrEmpty(postRequest.getContent()))
            throw  new NuestroException("Content is empty");
    }
}
