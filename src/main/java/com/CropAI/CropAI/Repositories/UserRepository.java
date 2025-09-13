package com.CropAI.CropAI.Repositories;
//package com.CropAI.CropAI.Repositories;

import com.CropAI.CropAI.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhone(String phone);

    // Find user by device's API key (embedded in devices list)
    @Query("{ 'devices.apiKey': ?0 }")
    User findUserByDeviceApiKey(String apiKey);
}


//package com.CropAI.CropAI.Repositories;
//
//import com.CropAI.CropAI.Entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByPhone(String phone);
//}
//
