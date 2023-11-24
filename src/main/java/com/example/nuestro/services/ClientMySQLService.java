package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import com.example.nuestro.services.interfaces.IClientDatabase;
import com.example.nuestro.shared.helpers.DatabaseHelper;
import jdk.jshell.spi.ExecutionControl;
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
import java.sql.SQLSyntaxErrorException;
import java.util.List;

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
            String createUsersTableSQL = """
    CREATE TABLE users (
        id VARCHAR(255) NOT NULL,
        created_at DATETIME(6) NULL DEFAULT NULL,
        creator_id VARCHAR(255) NULL DEFAULT NULL,
        deleted_at DATETIME(6) NULL DEFAULT NULL,
        birth_date DATE NULL DEFAULT NULL,
        database_type VARCHAR(50) CHECK (database_type IN ('None', 'MYSQL', 'MONGODB', 'MSSQL')) NULL DEFAULT NULL,
        db_database VARCHAR(255) NULL DEFAULT NULL,
        db_password VARCHAR(255) NULL DEFAULT NULL,
        db_port VARCHAR(255) NULL DEFAULT NULL,
        db_server VARCHAR(255) NULL DEFAULT NULL,
        db_username VARCHAR(255) NULL DEFAULT NULL,
        email VARCHAR(255) NULL DEFAULT NULL,
        first_name VARCHAR(255) NULL DEFAULT NULL,
        last_name VARCHAR(255) NULL DEFAULT NULL,
        password VARCHAR(255) NULL DEFAULT NULL,
        role VARCHAR(50) CHECK (role IN ('User', 'Admin')) NULL DEFAULT NULL,
        PRIMARY KEY (id),
        CONSTRAINT UK_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
    );
    """;

            String createUsersTableSQL2 = "CREATE TABLE IF NOT EXISTS users (" +
                    "id VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "created_at DATETIME(6) NULL DEFAULT NULL," +
                    "creator_id VARCHAR(255) NULL DEFAULT NULL," +
                    "deleted_at DATETIME(6) NULL DEFAULT NULL," +
                    "birth_date DATE NULL DEFAULT NULL," +
                    "connection_string VARCHAR(255) NULL DEFAULT NULL," +
                    "database_type ENUM('MYSQL','MONGODB','MSSQL') NULL DEFAULT NULL," +
                    "db_database VARCHAR(255) NULL DEFAULT NULL," +
                    "db_password VARCHAR(255) NULL DEFAULT NULL," +
                    "db_port VARCHAR(255) NULL DEFAULT NULL," +
                    "db_server VARCHAR(255) NULL DEFAULT NULL," +
                    "db_username VARCHAR(255) NULL DEFAULT NULL," +
                    "email VARCHAR(255) NULL DEFAULT NULL," +
                    "first_name VARCHAR(255) NULL DEFAULT NULL," +
                    "last_name VARCHAR(255) NULL DEFAULT NULL," +
                    "password VARCHAR(255) NULL DEFAULT NULL," +
                    "role ENUM('User','Admin') NULL DEFAULT NULL" +
                    ")";
            jdbcTemplate.execute(createUsersTableSQL);
        }

        if(!doesTableExist("posts")){
            // Create the posts table
            String createPostsTableSQL2 = "CREATE TABLE IF NOT EXISTS posts (" +
                    "id VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "created_at DATETIME(6) NULL DEFAULT NULL," +
                    "creator_id VARCHAR(255) NULL DEFAULT NULL," +
                    "content TEXT," +
                    "user_id VARCHAR(255)" +
                    ")";
            var createPostsTableSQL = """
    CREATE TABLE posts (
        id VARCHAR(255) NOT NULL,
        created_at DATETIME(6) NULL DEFAULT NULL,
        creator_id VARCHAR(255) NULL DEFAULT NULL,
        deleted_at DATETIME(6) NULL DEFAULT NULL,
        content VARCHAR(255) NULL DEFAULT NULL,
        user_id VARCHAR(255) NULL DEFAULT NULL,
        PRIMARY KEY (id),
        CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION
    );
    """;
            var createIndexSQL = """
    CREATE INDEX FK5lidm6cqbc7u4xhqpxm898qme ON posts (user_id);
    """;
            jdbcTemplate.execute(createPostsTableSQL);
            jdbcTemplate.execute(createIndexSQL);
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
}
