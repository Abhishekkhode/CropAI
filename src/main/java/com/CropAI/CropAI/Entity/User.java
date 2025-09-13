package com.CropAI.CropAI.Entity;

//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import lombok.Getter;
//import lombok.Setter;
//
//@Document(collection = "users")
//@Getter
//@Setter
//@Data
//public class    User {
//
//    @Id
//    private String id;
//
//    private String phone;
//    private String username;
//    private String email;
//    private String address;
//    private String otp;
//    private boolean verified;
//}



//package com.CropAI.CropAI.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;  // MongoDB uses String ObjectId

    private String phone;
    private String otp;
    private boolean verified = false;

    private String name;
    private String email;
    private String address;

    private List<Device> devices;

    // Getters and Setters
}
 class Device {
    private String deviceId;
    private String apiKey;
    // getters & setters
}
