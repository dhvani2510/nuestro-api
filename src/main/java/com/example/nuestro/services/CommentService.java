package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.Comment;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.models.post.PostResponse;
import com.example.nuestro.models.comment.CommentResponse;
import com.example.nuestro.models.comment.CommentRequest;
import com.example.nuestro.repositories.CommentRepository;
import com.example.nuestro.repositories.LikeRepository;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CommentService {
    private  final PostRepository postRepository;
    private  final  UserService userService;
    private  final ClientDatabaseService clientService;
    private static final Logger logger= LoggerFactory.getLogger(PostService.class);

    public CommentService(PostRepository postRepository, UserService userService, ClientDatabaseService clientService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.clientService = clientService;
    }

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentResponse> GetComment(String post_id){
        logger.info("Getting user {} Comments", post_id);
        List<Comment> comments= commentRepository.findByPostId(post_id);
        List<CommentResponse> result = comments.stream()
                .map(u -> new CommentResponse(u))
                .toList();
        return result;
    }

    private  void ValidateCommentRequest(CommentRequest commentRequest) throws NuestroException {
        if(StringHelper.StringIsNullOrEmpty(commentRequest.getComment()))
            throw  new NuestroException("Comment is empty");
    }

    @Transactional(rollbackFor = Exception.class)
    public  CommentResponse CreateComment(CommentRequest commentRequest) throws Exception {
        logger.info("User adding post with content {}",commentRequest.getComment());

        ValidateCommentRequest(commentRequest);

        var user= userService.GetUserContextInstance();
        if(user==null){
            logger.error("User not found");
            throw  new NuestroException("User not found");
        }

        var comment= new Comment(commentRequest.getComment(),user);

        //jdbcTemplate.setDataSource(userDataSource);

        if(user.getDatabaseType()!= DatabaseType.None){
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.addComment(comment);
        }
        commentRepository.save(comment); // Synchronize
        return  new CommentResponse(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommentResponse UpdateComment(String id, CommentRequest commentRequest) throws Exception {
        logger.info("User updating post {} with content {}" ,id, commentRequest.getComment());

        ValidateCommentRequest(commentRequest);

        var comment= commentRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Comment not find"));

        comment.Set(commentRequest);
        if(comment.getUser().getDatabaseType()!= DatabaseType.None){
            var user= userService.GetUser(comment.getUser().getId());
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.updatePost(comment);
        }
        commentRepository.save(comment);//synchonize
        return new CommentResponse(comment);
    }
    @Transactional(rollbackFor = Exception.class)
    public  void Delete(String id) throws Exception {
        logger.info("User deleting comment {}",id);
        var comment= commentRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Comment not find"));
        //post.setDeletedAt(LocalDateTime.now()) ; postRepository.save(post);
        //clientService.deletePost(post.getId());
        var user= userService.GetUser(comment.getUser().getId());
        var clientDatabase= clientService.getDatabase(user);
        clientDatabase.deletePost(comment.getId());
        commentRepository.delete(comment); //Synchonize
        logger.info("Comment deleted successfully");
    }
}
