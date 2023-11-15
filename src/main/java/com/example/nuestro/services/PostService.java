package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.models.post.PostResponse;
import com.example.nuestro.models.post.SearchPostRequest;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<PostResponse> GetPosts(){
        List<Post> posts= postRepository.findAll();
        List<PostResponse> result = posts.stream()
                .map(u -> new PostResponse(u))
                .toList();
        return result;
    }

    public List<PostResponse> SearchPosts(SearchPostRequest searchPostRequest){
      logger.info("Searching posts with {}", searchPostRequest.getKeyword());

        //StringHelper.StringIsNullOrEmpty(searchPostRequest.getUserId() )
        //NOT working postRepository.findByContentContainingAndUser_Id(searchPostRequest.getKeyword(), searchPostRequest.getUserId());
        List<Post> posts= postRepository.findByContentContaining(searchPostRequest.getKeyword());

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

    public  PostResponse UpdatePost(String id, PostRequest postRequest) throws Exception {
        logger.info("User updating post {} with content {}" ,id, postRequest.getContent());

        ValidatePostRequest(postRequest);

        var post= postRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Post not find"));

        post.Set(postRequest);
        if(post.getUser().getDatabaseType()!= DatabaseType.None){
            var user= userService.GetUser(post.getUser().getId());
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.updatePost(post);
        }
        postRepository.save(post);//synchonize
        return new PostResponse(post);
    }

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


//    private void addUser(User user, JdbcTemplate jdbcTemplate) {
//        String sql = "INSERT INTO users (id,created_at, creator_id, content, user_id) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql, user.getId(), user.getCreatedAt(), user.getCreatorId(), user.getContent(), post.getUser().id);
//    }
    //Add the number of reads over time? Big Long,



}
