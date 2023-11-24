package com.example.nuestro.services;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.UserModel;
import com.example.nuestro.services.interfaces.IClientDatabase;
import com.example.nuestro.shared.exceptions.NuestroException;
import com.example.nuestro.shared.helpers.DatabaseHelper;
import com.example.nuestro.shared.helpers.EncryptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class ClientDatabaseService
{
   // @Autowired
    //private final ClientMySQLService clientMySQLService;
    //private  final  ClientMongoDbService clientMongoDbService;
    private static final Logger logger= LoggerFactory.getLogger(ClientDatabaseService.class);
    public ClientDatabaseService() {
    }
    //public ClientService(JdbcTemplate jdbcTemplate) { this.jdbcTemplate= jdbcTemplate;  }

    public IClientDatabase getDatabase(User user) throws Exception {

        if(user.getDatabaseType()== DatabaseType.MYSQL){

            DataSource userDataSource =DatabaseHelper.createMySQLDataSource(user.getConnectionString());
            //DatabaseHelper.createDataSource(url, username, password);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
            return new ClientMySQLService(jdbcTemplate);// createDatabaseAndTables(user.getDbDatabase());
            ///var result= isConnectionValid();
        }
        else  if(user.getDatabaseType()==DatabaseType.MONGODB){
            var mongoClient =DatabaseHelper
                    //.createMongoClient(user);
            .createMongoClient(user.getConnectionString());
            var mongoTemplate= new MongoTemplate(mongoClient, EncryptionHelper.decrypt(user.getDbDatabase()));
            return new ClientMongoDbService(mongoTemplate);
        }
        else  if(user.getDatabaseType()==DatabaseType.MSSQL){
            var mssqlDataSource =DatabaseHelper.createMSSQLDataSource(user.getConnectionString());

            var sqlTemplate= new JdbcTemplate(mssqlDataSource);
            return new ClientMSSQLService(sqlTemplate);
        }
        else if(user.getDatabaseType()==DatabaseType.None){
            return null;
        }
        else {
            throw  new NuestroException("Database type not supported");
        }
    }
}
