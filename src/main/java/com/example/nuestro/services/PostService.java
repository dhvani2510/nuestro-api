package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.Like;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.like.LikeResponse;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.models.post.PostResponse;
import com.example.nuestro.models.post.SearchPostRequest;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.repositories.LikeRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.crypto.Data;
import java.util.List;

@Service
public class PostService
{
    private  final PostRepository postRepository;
    private  final  UserService userService;
    private  final ClientDatabaseService clientService;
    private static final Logger logger= LoggerFactory.getLogger(PostService.class);

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, ClientDatabaseService clientService){

        this.postRepository = postRepository;
        this.userService = userService;
        this.clientService = clientService;
    }

    @Autowired
    private LikeRepository likeRepository;

    public List<PostResponse> GetPosts(){
        logger.info("Getting posts");
        List<Post> posts= postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponse> result = posts.stream()
                .map(u -> new PostResponse(u))
                .toList();
        return result;
    }

    public List<PostResponse> GetPosts(String userId) {
        logger.info("Getting user {} posts", userId);

        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<PostResponse> result = posts.stream()
                .map(u -> new PostResponse(u))
                .toList();

        return result;
    }

    public List<PostResponse> SearchPosts(SearchPostRequest searchPostRequest) {
        logger.info("Searching posts with {}", searchPostRequest.getKeyword());

        long startTime = System.currentTimeMillis();

        List<Post> posts;
        if (!StringHelper.StringIsNullOrEmpty(searchPostRequest.getUserId())) {
            posts = postRepository.findByContentContainingAndUser_IdOrderByCreatedAtDesc(
                    searchPostRequest.getKeyword(), searchPostRequest.getUserId()
            );
        } else {
            posts = postRepository.findByContentContainingOrderByCreatedAtDesc(searchPostRequest.getKeyword());
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.info("SearchPosts method execution time: {} milliseconds", executionTime);

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

    public  Post GetPostInstance(String id) throws NuestroException {
        logger.info("Getting post {}", id);
        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not found"));
        return post;
    }


    @Transactional(rollbackFor = Exception.class)
    public  PostResponse CreatePost(PostRequest postRequest) throws Exception {
        logger.info("User adding post with content {}",postRequest.getContent());

        ValidatePostRequest(postRequest);

        var user= userService.GetUserContextInstance();
        if(user==null){
            logger.error("User not found");
            throw  new NuestroException("User not found");
        }

        var post= new Post(postRequest.getContent(),user);

        //jdbcTemplate.setDataSource(userDataSource);

        if(user.getDatabaseType()!= DatabaseType.None){
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.addPost(post);
        }
        postRepository.save(post); // Synchronize
        return  new PostResponse(post);
    }

    @Transactional(rollbackFor = Exception.class)
    public PostResponse UpdatePost(String id, PostRequest postRequest) throws Exception {
        logger.info("User updating post {} with content {}" ,id, postRequest.getContent());

        ValidatePostRequest(postRequest);

        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));

        post.Set(postRequest);
        post.setUpdaterId(post.getUser().getId());
        if(post.getUser().getDatabaseType()!= DatabaseType.None){
            var user= userService.GetUser(post.getUser().getId());
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.updatePost(post);
        }
        postRepository.save(post);//synchonize
        return new PostResponse(post);
    }
    @Transactional(rollbackFor = Exception.class)
    public  void Delete(String id) throws Exception {
        logger.info("User deleting post {}",id);
        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));
        //post.setDeletedAt(LocalDateTime.now()) ; postRepository.save(post);
       //clientService.deletePost(post.getId());
        var user= userService.GetUser(post.getUser().getId());
        var clientDatabase= clientService.getDatabase(user);
        clientDatabase.deletePost(post.getId());
        postRepository.delete(post); //Synchonize
        logger.info("Post deleted successfully");
    }

    private  void ValidatePostRequest(PostRequest postRequest) throws NuestroException {
        if(StringHelper.StringIsNullOrEmpty(postRequest.getContent()))
            throw  new NuestroException("Content is empty");
    }

    @Transactional(rollbackFor = Exception.class)
    public LikeResponse LikePost(String id) throws Exception {

        logger.info("User likes post {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));
        // Add a like to the post
        var user= userService.GetUserContextInstance();

        //check if the post was already liked
        var liked= likeRepository.findByPost_IdAndUser_Id(post.getId(),user.getId());
        if(liked!= null)
            throw new NuestroException("User already liked post");

        var like = new Like(user, post);
        likeRepository.save(like);
        if(post.getUser().getDatabaseType()!= DatabaseType.None){
            var clientDatabase= clientService.getDatabase(user);
            //TODO
            //clientDatabase.likePost(like);
        }

        return new LikeResponse(like.getId(), post.getId(), user.getId());
    }


//    private void addUser(User user, JdbcTemplate jdbcTemplate) {
//        String sql = "INSERT INTO users (id,created_at, creator_id, content, user_id) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql, user.getId(), user.getCreatedAt(), user.getCreatorId(), user.getContent(), post.getUser().id);
//    }
    //Add the number of reads over time? Big Long,
}
