package com.example.nuestro.services;

import com.example.nuestro.entities.Comment;
import com.example.nuestro.entities.Like;
import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import com.example.nuestro.services.interfaces.IClientDatabase;
import com.example.nuestro.shared.helpers.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ClientMySQLService implements IClientDatabase
{
   // @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger= LoggerFactory.getLogger(ClientMySQLService.class);
    public ClientMySQLService() {  }
    public ClientMySQLService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate= jdbcTemplate;
        //isConnectionValid();
    }

    public  void setDatabase(JdbcTemplate jdbcTemplate, User user) throws Exception {

        this.jdbcTemplate= jdbcTemplate;
        createDatabaseAndTables(user.getDbDatabase()); //decrypt?
        ///var result= isConnectionValid();
    }
    public boolean isConnectionValid() {
        try {
            // Execute a ConnectionCallback to check the connection validity
            jdbcTemplate.execute(new ConnectionCallback<Void>() {
                @Override
                public Void doInConnection(Connection connection) throws SQLException {
                    if (connection.isValid(5)) { // 5 seconds timeout (adjust as needed)
                        System.out.println("Connection is valid.");
                    } else {
                        System.out.println("Connection is not valid.");
                    }
                    return null;
                }
            });
            return true;
        } catch (Exception e) {
            // Handle any exceptions here
            logger.error(e.getMessage());
            return false;
        }
    }

    public Post getPostById(String postId) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{postId}, new BeanPropertyRowMapper<>(Post.class));
    }

    public List<Post> getPosts() {

        //String sql = "SELECT posts.*, users.* FROM posts INNER JOIN users ON posts.user_id = users.id";
        //return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Post.class));


        String sql = "SELECT * FROM posts";
        //List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        var posts= jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Post.class));
        var userIds= posts.stream().map(p->p.getUserId()).toList();
        // var users= getUsers(userIds);
        return posts;
    }

    public void addRangePost(List<Post> posts) {
        String sql = "INSERT INTO posts (id, created_at, creator_id, content, user_id) VALUES (?, ?, ?, ?, ?)";

        for (Post post : posts) {
            jdbcTemplate.update(
                    sql,
                    post.getId(),
                    post.getCreatedAt(),
                    post.getCreatorId(),
                    post.getContent(),
                    post.getUser().getId()
            );
        }
    }

    public void addPost(Post post) {
        String sql = "INSERT INTO posts (id,created_at, creator_id, content, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getId(), post.getCreatedAt(), post.getCreatorId(), post.getContent(), post.getUser().getId());
    }

    public void updatePost(Post post) {
        String sql = "UPDATE posts SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, post.getContent(), post.getId());
    }

    public void deletePost(String postId) {
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, postId);
    }

    //TODO check this
    public void likePost(Like like) {
        String sql = "INSERT INTO likes (id, created_at, creator_id, post_id, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                like.getId(),
                like.getCreatedAt(),
                like.getCreatorId(),
                like.getPost().getId(),
                like.getUser().getId()
        );
    }

    public void deleteLike(String likeId) {
        String sql = "DELETE FROM likes WHERE id = ?";
       var result= jdbcTemplate.update(sql, likeId);
    }

    public List<Like> getLikes() {
        String sql = "SELECT * FROM likes";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Like.class));
    }

    public List<Comment> getComments() {
        String sql = "SELECT * FROM comments";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
    }
    public User getUserById2(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(User.class));
    }
    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            // Handle the case when no user with the specified ID is found
            return null; // Or throw a custom exception or perform any other desired action
        }
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }
    public List<User> getUsers(List<String> userIds) {
        // Check if the list is empty to avoid SQL errors
        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Generate a comma-separated string of user IDs for the SQL query
        String idList = String.join(",", userIds);

        // Use a parameterized query to avoid SQL injection
        String sql = "SELECT * FROM users WHERE id IN (" + idList + ")";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public void addUser(User user) throws Exception {
        String sql = "INSERT INTO users " +
                "(id, created_at, creator_id, deleted_at, database_type, " +
                "db_database, db_password, db_port, db_server, db_username, email, first_name, last_name, " +
                "password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getId(),
                user.getCreatedAt(),
                user.getCreatorId(),
                user.getDeletedAt(),
                user.getDatabaseType().toString(),
                user.getDbDatabase(),
                user.getDbPassword(),
                user.getDbPort(),
                user.getDbServer(),
                user.getDbUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword()
        );
    }

    public void updateUser(User user) throws Exception {
        String sql = "UPDATE users " +
                "SET database_type = ?, " +
                "db_database = ?, db_password = ?, db_port = ?, db_server = ?, db_username = ?, " +
                "email = ?, first_name = ?, last_name = ?, password = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                user.getDatabaseType().toString(),
                user.getDbDatabase(),
                user.getDbPassword(),
                user.getDbPort(),
                user.getDbServer(),
                user.getDbUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getId()
        );
    }

    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void createDatabaseAndTables(String databaseName) {
        // Create the database if it doesn't exist
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS `" + databaseName+"`";
        try {
            jdbcTemplate.execute(createDatabaseSQL);
        } catch (Exception e) {
            // Log more details about the exception
             logger.error(e.getMessage()); //e.printStackTrace();
            throw e; // Optionally rethrow the exception or handle it accordingly
        }

        // Switch to the created database
        String useDatabaseSQL = "USE `"+databaseName +"`";
        jdbcTemplate.execute(useDatabaseSQL);

        if(!doesTableExist("users")){
            // Create the users table

           var createUsersTableSQL= DatabaseHelper.readResourceFile("mysql/users_create_table.sql");
            jdbcTemplate.execute(createUsersTableSQL);
        }

        if(!doesTableExist("posts")){
            // Create the posts table
            var createPostsTableSQL = DatabaseHelper.readResourceFile("mysql/posts_create_table.sql");
            var createIndexSQL = """
    CREATE INDEX FK5lidm6cqbc7u4xhqpxm898qme ON posts (user_id);
    """;
            jdbcTemplate.execute(createPostsTableSQL);
            //jdbcTemplate.execute(createIndexSQL);
        }
        if(!doesTableExist("likes")){
            var createLikesTableSQL= DatabaseHelper.readResourceFile("mysql/likes_create_table.sql");
            jdbcTemplate.execute(createLikesTableSQL);
        }
        if(!doesTableExist("comments")){
            var createCommentsTableSQL= DatabaseHelper.readResourceFile("mysql/comments_create_table.sql");
            jdbcTemplate.execute(createCommentsTableSQL);
        }
//        if(!doesTableExist("audit_log")){ //NOT working
//            var createAuditLogsTableSQL= DatabaseHelper.readResourceFile("mysql/audit_log_create_table.sql");
//            jdbcTemplate.execute(createAuditLogsTableSQL);
//        }
    }

    public boolean doesTableExist2(String tableName) {
        String sql = "SELECT 1 FROM " + tableName + " LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class);
            return true; // Table exists
        } catch (Exception e) {
            return false; // Table does not exist
        }
    }

    public boolean doesTableExist(String tableName) {
        String sql = "SELECT 1 FROM " + tableName + " LIMIT 1";
        try {
            jdbcTemplate.query(sql, (rs, rowNum) ->null);
            return true; // Table exists
        } catch (Exception e) {
            return false; // Table does not exist
        }
    }

    @Override
    public boolean doesDatabaseExist(String databaseName) {
        String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";

        // Use queryForObject with a try-catch block to handle EmptyResultDataAccessException
        try {
            String result = jdbcTemplate.queryForObject(query, String.class, databaseName);
            return result != null && result.equals(databaseName);
        } catch (Exception e) {
            return false;
        }
    }

    public void AddToClientDatabase (User user, List<Post> posts) throws Exception
    {    logger.info("Connecting to client database, creating user and posts");

        var userInClientDb= getUserById(user.getId());
        if(userInClientDb ==null)
            addUser(user);

        if( posts!=null&& (long) posts.size() !=0){
            addRangePost(posts);
        }
    }

    public void AddToClientDatabase2 (User user, List<Post> posts) throws Exception {
        logger.info("Connecting to client database, creating user and posts");
        var connectionString = DatabaseHelper
                .GenerateMySQLConnectionString(user.getDbServer(),
                        Integer.parseInt(user.getDbPort()),
                        user.getDbDatabase(),
                        user.getDbUsername(),
                        user.getDbPassword());

        //var post= new Post(postRequest.getContent(),user);
        //addPost(url, userame, password, postRequest.getContent(),post);
        // Create a data source using the user's connection information
        DataSource userDataSource =DatabaseHelper.createMySQLDataSource(connectionString);
        //DatabaseHelper.createDataSource(url, username, password);

        var jdbcTemplate = new JdbcTemplate(userDataSource);
        //jdbcTemplate.setDataSource(userDataSource);
        //addPost(post);
        //postRepository.save(post); // Synchronize
        setDatabase(jdbcTemplate, user);
        var userInClientDb= getUserById(user.getId());
        if(userInClientDb ==null)
            addUser(user);

        if( posts!=null&& (long) posts.size() !=0){
            addRangePost(posts);
        }
    }
    public void addComment(Comment comment) {
        String sql = "INSERT INTO comments (id,created_at, creator_id, comment, user_id, post_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getId(), comment.getCreatedAt(), comment.getCreatorId(), comment.getComment(), comment.getUser().getId(), comment.getPost().getId());
    }

    public void updateComment(Comment comment) {
        String sql = "UPDATE comments SET comment = ? WHERE id = ?";
        jdbcTemplate.update(sql, comment.getComment(), comment.getId());
    }

    public void deleteComment(String commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        var result =jdbcTemplate.update(sql, commentId);
    }
}
