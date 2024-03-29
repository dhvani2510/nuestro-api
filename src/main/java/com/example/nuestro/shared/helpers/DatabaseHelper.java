package com.example.nuestro.shared.helpers;

import com.example.nuestro.entities.User;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.FileCopyUtils;

import javax.sql.DataSource;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper
{
    public static DataSource createMySQLDataSource(String jdbcUrl, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
    public static DataSource createMySQLDataSource(String jdbcUrl) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(jdbcUrl);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //dataSource.setUrl(jdbcUrl);
        return dataSource;
    }

    public static DataSource createMSSQLDataSource(String server, int port, String databaseName, String username, String password) {

//        var username= user.getDbUsername();
//        var password= user.getDbPassword();
//        var server= user.getDbServer();
//        var port= Integer.parseInt(user.getDbPort());
//        var database= user.getDbDatabase();

        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL("jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + databaseName);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public static DataSource createMSSQLDataSource(String jdbcUrl) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(jdbcUrl);
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return dataSource;
    }

    public static String GenerateMSSQLConnectionString(String server, int port, String databaseName, String username, String password) {
        return String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;trustServerCertificate=true;", server, port, databaseName, username, password);
    }

    public static MongoClient createMongoClient(String mongoURI) {
        // Create a MongoDB client using the provided URI

        return MongoClients.create(mongoURI);
    }
    public static MongoClient createMongoClient(User user) throws Exception {
        var username= EncryptionHelper.decrypt(user.getDbUsername());
        var password= EncryptionHelper.decrypt(user.getDbPassword());

        if(StringHelper.StringIsNullOrEmpty(username) && StringHelper.StringIsNullOrEmpty((password))){
            return  createMongoClient(user.getConnectionString());
        }
        var server= EncryptionHelper.decrypt(user.getDbServer());
        var port= Integer.parseInt(EncryptionHelper.decrypt(user.getDbPort()));
        var database= EncryptionHelper.decrypt(user.getDbDatabase());

        return createMongoClient(server,port,database,username,password);
    }

    public static MongoClient createMongoClient(String server, int port, String databaseName, String username, String password) {
        MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(server, port))))
                .build();
        return MongoClients.create(settings);
    }

    public static MongoDatabase getDatabase(MongoClient mongoClient, String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    public static String GenerateMySQLConnectionString(String server, int port, String databaseName, String username, String password) {
        // Build the connection string
        return String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", server, port, databaseName, username, password);
    }

    public static String GenerateMongoDBURI(String server, int port, String databaseName, String username, String password) throws UnsupportedEncodingException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return String.format("mongodb://%s:%d/%s", server, port, databaseName);
        } else {
            username = URLEncoder.encode(username, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
            return String.format("mongodb://%s:%s@%s:%d/%s?authSource=admin", username, password, server, port, databaseName);
        }
    }

//    private String getSqlScript( String scriptPath) {
//
//        Resource resource = new ClassPathResource(scriptPath);
//        try (InputStream inputStream = resource.getInputStream()) {
//            String sqlScript = new String(IOUtils.toByteArray(inputStream), StandardCharsets.UTF_8);
//           return sqlScript;
//        } catch (IOException e) {
//           System.out.print(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static String loadSqlFileAsString(String filePath) {
        try {
            File file = new File(ClassLoader.getSystemClassLoader().getResource(filePath).getFile());
            byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
            return new String(bytes);
        } catch (IOException e) {
            // Handle the exception (e.g., log it, throw a custom exception, etc.)
            e.printStackTrace();
            return null; // or throw a custom exception if appropriate
        }
    }


    public static String loadQueryFromFile(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            StringBuilder queryBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(resource.getInputStream())))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    queryBuilder.append(line).append("\n");
                }
            }

            return queryBuilder.toString();
        } catch (IOException e) {
            // Handle the exception (e.g., log it, throw a custom exception, etc.)
            e.printStackTrace();
            return null; // or throw a custom exception if appropriate
        }
    }

    public static String readResourceFile(String filePath) {
        try {
            Resource resource = new ClassPathResource(filePath);

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (IOException e) {
            // Handle the exception (e.g., log it, throw a custom exception, etc.)
            e.printStackTrace();
            return null; // or throw a custom exception if appropriate
        }
    }
}
