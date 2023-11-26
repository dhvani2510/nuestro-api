package com.example.nuestro.services;

import com.example.nuestro.configurations.JwtService;
import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.models.UserModel;
import com.example.nuestro.models.auth.AuthenticationRequest;
import com.example.nuestro.models.auth.AuthenticationResponse;
import com.example.nuestro.models.auth.RegisterRequest;
import com.example.nuestro.models.auth.RegisterResponse;
import com.example.nuestro.models.users.ProfileRequest;
import com.example.nuestro.models.users.ProfileResponse;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final SynchronizeService synchronizeService;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    private  final PasswordEncoder passwordEncoder;

    private  final ClientDatabaseService clientService;

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Autowired
    public  UserService(UserRepository userRepository, SynchronizeService synchronizeService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, ClientDatabaseService clientService){
        this.userRepository=userRepository;
        this.synchronizeService = synchronizeService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;
    }
    public List<UserModel> GetUsers(){

        List<User> users= userRepository.findAll();
        List<UserModel> result = users.stream()
                .map(u -> new UserModel(u))
                .toList();
        return result;
    }
    public List<User> GetAllUsers(){

        return userRepository.findAll();
    }

    public  UserModel GetProfile(String id) throws NuestroException {
        logger.info("Getting user {} profile",id);

        var user= userRepository.findById(id).orElseThrow(()-> new NuestroException("User not found"));
        return new UserModel(user);
    }

    @Transactional(rollbackFor = Exception.class)
   public ProfileResponse UpdateProfile(ProfileRequest profileRequest) throws NuestroException, IOException {
       if(StringIsNullOrEmpty(profileRequest.getFirstName()))
           throw new NuestroException("First name is empty");
       if(StringIsNullOrEmpty(profileRequest.getLastName()))
           throw new NuestroException("Last name is empty");

       logger.info("Getting user profile from context");
       var auth= SecurityContextHolder.getContext().getAuthentication();
       if(!auth.isAuthenticated()){
           var details= auth.getDetails();
           logger.error("User {} is not authenticated", details);
           throw new NuestroException("user is not authenticated");
       }
       var user = userRepository.findByEmail(auth.getName())
               .orElseThrow(()-> new NuestroException("User not found")); // name should contain the enail
       //var user= (User)auth.getPrincipal();//var user= userRepository.findById(((User)auth.getPrincipal()))

       user.setFirstName(profileRequest.getFirstName());
       user.setLastName( profileRequest.getLastName());
       
       userRepository.save(user);

       logger.info("User profile updated");
       return new ProfileResponse(user);
     }

    @Transactional(rollbackFor = Exception.class)
    public ProfileResponse UpdateDatabase(UpdateDatabaseRequest updateDatabaseRequest) throws Exception {

        logger.info("User is updating database connection");
        if(StringIsNullOrEmpty(updateDatabaseRequest.getDatabase()))
            throw new NuestroException("Database is empty");
        if(StringIsNullOrEmpty(updateDatabaseRequest.getServer()))
            throw new NuestroException("Server is empty");
        if(StringIsNullOrEmpty(updateDatabaseRequest.getPort()))
            throw new NuestroException("Port is null");

        logger.info("Getting user profile from context");
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw new NuestroException("user is not authenticated");
        }
        var user = userRepository.findByEmail(auth.getName())
                .orElseThrow(()-> new NuestroException("User not found")); // name should contain the enail
        //var user= (User)auth.getPrincipal();//var user= userRepository.findById(((User)auth.getPrincipal()))
        user.setUpdaterId(user.getCreatorId());
        user.setUpdatedAt(LocalDateTime.now());
        user.Update(updateDatabaseRequest);
        var clientDatabase= clientService.getDatabase(user);
        var exists=clientDatabase.doesDatabaseExist(user.getDbDatabase());
        if(!clientDatabase.isConnectionValid()){
            throw  new NuestroException("Failed to connect to database. Check if database is created");
        }
        else{
            clientDatabase.createDatabaseAndTables(user.getDbDatabase());
        }

        clientDatabase.updateUser(user);

        synchronizeService.synchronizeData(user.getId()); //synchornize

        userRepository.save(user);
        logger.info("Database updated");
        return new ProfileResponse(user);
    }

    public  void Delete(String id) throws Exception {
        logger.info("User deleting post {}",id);
        var user= userRepository.findById(id)
                .orElseThrow(()-> new NuestroException("User not find"));
        //post.setDeletedAt(LocalDateTime.now()) ; postRepository.save(post);

        var clientDatabase= clientService.getDatabase(user);
        clientDatabase.deleteUser(user.getId());
        userRepository.delete(user); //Synchonize
        logger.info("User deleted successfully");
    }

    public  UserModel GetUser() throws NuestroException {
        var user=GetUserContext();
//        var u= userRepository.findById(user.Id)
//                .orElseThrow(()-> new NuestroException("User not found"));
        if(user== null)
            throw  new NuestroException("User not found");

        return new UserModel(user);
    }

    public  User GetUser(String id) throws NuestroException {

        return userRepository.findById(id)
                .orElseThrow(()-> new NuestroException("User not found"));
    }

    public  User GetUserContext() throws NuestroException {
        logger.info("Getting user profile");
        var auth= SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()){ // toggle this
            var details= auth.getDetails();
            logger.error("User {} is not authenticated", details);
            throw  new NuestroException("user is not authenticated");
        }

        return (User)auth.getPrincipal();
    }

    public  User GetUserContextInstance() throws NuestroException {
        var user= GetUserContext();
        return userRepository.findById(user.getId())
                .orElseThrow(()-> new NuestroException("User not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest registerRequest) throws NuestroException {

        logger.info("User is registering with email {} and name {}", registerRequest.getEmail(), registerRequest.getFirstName());
        // Check if auto mapper exist in Java
        if(StringIsNullOrEmpty(registerRequest.getEmail()))
            throw  new NuestroException("Email is empty");
        if(!emailIsValid(registerRequest.getEmail()))
            throw  new NuestroException("Email is not valid");
        if(StringIsNullOrEmpty(registerRequest.getPassword()))
            throw new NuestroException("Password is empty");

        if(StringIsNullOrEmpty(registerRequest.getFirstName()))
            throw new NuestroException("Name is empty");
        if(StringIsNullOrEmpty(registerRequest.getLastName()))
            throw new NuestroException("Surname is empty");

        var existinguser= userRepository.findByEmail(registerRequest.getEmail());

        if(!existinguser.isEmpty())
        {
            logger.error("User {} already exits", existinguser.get().getEmail() );
            throw new NuestroException("User exists already");
        }
        var hashedPassword= //hashWith256(registerRequest.getPassword());
        passwordEncoder.encode(registerRequest.getPassword());
        var user= new User.UserBuilder(registerRequest.getFirstName(),registerRequest.getLastName())
                .setEmail(registerRequest.getEmail())
                .setPassword(hashedPassword)
                .setRole(Role.User)
                .build();

        user.setDatabaseType(DatabaseType.None);
        userRepository.save(user);

        var response= new RegisterResponse(user.getId(),user.getFirstName(), user.getLastName(),user.getEmail());
        return  response;
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws NuestroException {

        var auth= SecurityContextHolder.getContext().getAuthentication();
        var details =auth.getDetails().toString();
        logger.info("User {} is logging in with email {}", details,authenticationRequest.getEmail());

        if(StringIsNullOrEmpty(authenticationRequest.getEmail()))
          throw  new NuestroException("Email is empty");
        if(StringIsNullOrEmpty(authenticationRequest.getPassword()))
            throw new NuestroException("Password is empty");

        var user= userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()-> new NuestroException("User not found"));

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        var extraClaims = new HashMap<String, Object>();
        var jwtToken= jwtService.GenerateToken(extraClaims, user);
        return new AuthenticationResponse.Builder()
                //.builder()
                .setToken(jwtToken)
                .build();
    }


    @Transactional(rollbackFor = Exception.class)
    public  void Delete() throws Exception {
        logger.info("Deleting user");

        var userContext= GetUserContext();
        var user= GetUser(userContext.getId());
        if(!Objects.equals(userContext.getId(), user.getId()))
            throw  new NuestroException("Not authorized to delete another user");

        //Synchonize?
//        if(user.getDatabaseType()!=DatabaseType.None){
//            var clientDatabase= clientService.getDatabase(user);
//            clientDatabase.deleteUser(user.getId());
//        }

        userRepository.delete(user);
        logger.info("User deleted successfully");
    }

    private  void authenticate(String email, String password) throws NuestroException {

        try{
            var result =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email,
                    password
            ));
            logger.info("User authenticated with the right credentials");
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw  new NuestroException("Wrong email or password");
        }

    }

    // Helper method
    private static boolean StringIsNullOrEmpty(String str){
        return (str == null && str.trim().isEmpty());
    }

    public static String hashWith256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
            // return  String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private  static boolean emailIsValid(String s){

        var EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return EMAIL.matcher(s).matches();
    }
}
