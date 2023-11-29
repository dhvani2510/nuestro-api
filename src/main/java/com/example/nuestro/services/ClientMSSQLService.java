package com.example.nuestro.services;

import com.example.nuestro.entities.Like;
import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.Comment;
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
import java.util.List;

@Service
public class ClientMSSQLService implements IClientDatabase
{
   // @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger= LoggerFactory.getLogger(ClientMSSQLService.class);
    public ClientMSSQLService() {  }
    public ClientMSSQLService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate= jdbcTemplate;
        //isConnectionValid();
    }

    public  void setDatabase(JdbcTemplate jdbcTemplate, User user) throws Exception {

        this.jdbcTemplate= jdbcTemplate;
        createDatabaseAndTables(user.getDbDatabase());
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
        String sql = "SELECT * FROM posts";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Post.class));
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

    public void likePost(Like like) {
        String sql = "INSERT INTO likes (id, created_at, creator_id, post_id, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                like.getId(),
                like.getCreatedAt(),
                like.getCreatorId(),
                like.getPost().getId(),
                like.getUser().getId()
        );
    }

    @Override
    public void deleteLike(String likeId) {
        String sql = "DELETE FROM likes WHERE id = ?";
        jdbcTemplate.update(sql, likeId);
    }

    @Override
    public List<Like> getLikes() {
        String sql = "SELECT * FROM likes";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Like.class));
    }

    @Override
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

    public void createDatabaseIfNotExists(String databaseName) {
        // Check if the database exists
        String checkExistsSQL = "SELECT 1 FROM sys.databases WHERE name = ?";
        List<Integer> result = jdbcTemplate.queryForList(checkExistsSQL, Integer.class, databaseName);

        if (result.isEmpty()) {
            // The database does not exist, so create it
            String createDatabaseSQL = "CREATE DATABASE " + databaseName;
            jdbcTemplate.execute(createDatabaseSQL);
        }
    }

    public void createDatabaseAndTables(String databaseName) {
        createDatabaseIfNotExists(databaseName);
        //String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS [" + databaseName+"]";
        //jdbcTemplate.execute(createDatabaseSQL);

        // Switch to the created database
        String useDatabaseSQL = "USE ["+databaseName +"]";
        jdbcTemplate.execute(useDatabaseSQL);

        if(!doesTableExist("dbo.users")){
            // Create the users table
            var createUsersTableSQL= DatabaseHelper.readResourceFile("mssql/users_create_table.sql");
            jdbcTemplate.execute(createUsersTableSQL);
        }
        if(!doesTableExist("posts")){
            // Create the posts table
            var createPostsTableSQL = DatabaseHelper.readResourceFile("mssql/posts_create_table.sql");
            jdbcTemplate.execute(createPostsTableSQL);
            //jdbcTemplate.execute(createIndexSQL);
        }
        if(!doesTableExist("likes")){
            var createLikesTableSQL= DatabaseHelper.readResourceFile("mssql/likes_create_table.sql");
            jdbcTemplate.execute(createLikesTableSQL);
        }
        if(!doesTableExist("comments")){
            var createCommentsTableSQL= DatabaseHelper.readResourceFile("mssql/comments_create_table.sql");
            jdbcTemplate.execute(createCommentsTableSQL);
        }
        if(!doesTableExist("audit_log")){
            var createAuditLogsTableSQL= DatabaseHelper.readResourceFile("mssql/audit_log_create_table.sql");
            jdbcTemplate.execute(createAuditLogsTableSQL);
        }
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

    public boolean doesDatabaseExist(String databaseName) {
        String sql = "SELECT name FROM sys.databases WHERE name = ?";
        try {
            jdbcTemplate.queryForObject(sql, String.class, databaseName);
            return true; // Database exists
        }
        catch (EmptyResultDataAccessException e) {
            logger.error("Database does not exist");
            return false; // Database does not exist
        }
    }

    public boolean doesTableExist4(String tableName) {
        String checkTableSQL = "IF OBJECT_ID('" + tableName + "', 'U') IS NOT NULL BEGIN END";

        try {
            jdbcTemplate.execute(checkTableSQL);
            return true; // Table exists
        } catch (Exception e) {
            return false; // Table does not exist
        }
    }

    public boolean doesTableExist3(String tableName) {
        String checkTableSQL = "IF OBJECT_ID('" + tableName + "', 'U') IS NOT NULL SELECT 1 ELSE SELECT 0";

        try {
            int result = jdbcTemplate.queryForObject(checkTableSQL, Integer.class);
            return result == 1; // Table exists
        } catch (EmptyResultDataAccessException e) {
            return false; // Table does not exist
        }
    }

    public boolean doesTableExist(String tableName) {
        String checkTableSQL = "IF OBJECT_ID('" + tableName + "', 'U') IS NOT NULL SELECT TOP 1 1 ELSE SELECT TOP 1 0";

        try {
            int result = jdbcTemplate.queryForObject(checkTableSQL, Integer.class);
            return result == 1; // Table exists
        } catch (EmptyResultDataAccessException e) {
            return false; // Table does not exist
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
                .GenerateMSSQLConnectionString(user.getDbServer(),
                        Integer.parseInt(user.getDbPort()),
                        user.getDbDatabase(),
                        user.getDbUsername(),
                        user.getDbPassword());

        //var post= new Post(postRequest.getContent(),user);
        //addPost(url, userame, password, postRequest.getContent(),post);
        // Create a data source using the user's connection information
        DataSource userDataSource =DatabaseHelper.createMSSQLDataSource(connectionString);
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
        String sql = "INSERT INTO comments (id,created_at, creator_id, comment, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getId(), comment.getCreatedAt(), comment.getCreatorId(), comment.getComment(), comment.getUser().getId());
    }

    public void updateComment(Comment comment) {
        String sql = "UPDATE comments SET comment = ? WHERE id = ?";
        jdbcTemplate.update(sql, comment.getComment(), comment.getId());
    }

    public void deleteComment(String commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, commentId);
    }
}
