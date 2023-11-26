package com.example.nuestro.services;

import com.example.nuestro.entities.Comment;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.comment.CommentResponse;
import com.example.nuestro.models.comment.CommentRequest;
import com.example.nuestro.repositories.CommentRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private  final PostService postService;
    private  final  UserService userService;
    private final CommentRepository commentRepository;
    private  final ClientDatabaseService clientService;
    private static final Logger logger= LoggerFactory.getLogger(PostService.class);

    @Autowired
    public CommentService(PostService postService, UserService userService, CommentRepository commentRepository, ClientDatabaseService clientService) {
        this.postService = postService;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.clientService = clientService;
    }

    public List<CommentResponse> GetComments(String postId){
        logger.info("Getting user {} comments", postId);
        List<Comment> comments= commentRepository.findByPostId(postId);
        List<CommentResponse> result = comments.stream()
                .map(u -> new CommentResponse(u))
                .toList();
        return result;
    }

    public CommentResponse GetComment(String id) throws NuestroException {
        logger.info("Getting comment {}", id);
        var comment= commentRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Comment not found"));
        return new CommentResponse(comment);
    }


    private  void ValidateCommentRequest(CommentRequest commentRequest) throws NuestroException {
        if(StringHelper.StringIsNullOrEmpty(commentRequest.getComment()))
            throw  new NuestroException("Comment is empty");
    }

    @Transactional(rollbackFor = Exception.class)
    public  CommentResponse CreateComment(CommentRequest commentRequest, String postId) throws Exception {
        logger.info("User adding comment with content {}",commentRequest.getComment());

        ValidateCommentRequest(commentRequest);

        var user= userService.GetUserContextInstance();
        var post= postService.GetPostInstance(postId);

        var comment= new Comment(commentRequest.getComment(), user, post);

        //jdbcTemplate.setDataSource(userDataSource);

        if(user.getDatabaseType()!= DatabaseType.None){
           // var clientDatabase= clientService.getDatabase(user);
            //clientDatabase.addComment(comment);
        }
        commentRepository.save(comment); // Synchronize
        return  new CommentResponse(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommentResponse UpdateComment(String id, CommentRequest commentRequest) throws Exception {
        logger.info("User updating comment {} with content {}" ,id, commentRequest.getComment());

        ValidateCommentRequest(commentRequest);

        var comment= commentRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Comment not find"));

        comment.Set(commentRequest);
        if(comment.getUser().getDatabaseType()!= DatabaseType.None){
            //var user= userService.GetUser(comment.getUser().getId());
            //var clientDatabase= clientService.getDatabase(user);
            //clientDatabase.updatePost(comment);
        }
        commentRepository.save(comment);//synchonize
        return new CommentResponse(comment);
    }
    @Transactional(rollbackFor = Exception.class)
    public  void Delete(String id) throws Exception {
        logger.info("User deleting comment {}",id);
        var comment= commentRepository.findById(id)
                .orElseThrow(()-> new NuestroException("Comment not find"));

       // var user= userService.GetUser(comment.getUser().getId());
        //var clientDatabase= clientService.getDatabase(user);
        //clientDatabase.deletePost(comment.getId());
        commentRepository.delete(comment); //Synchonize
        logger.info("Comment deleted successfully");
    }
}
