package com.CropAI.CropAI.Controller;

import com.CropAI.CropAI.Entity.User;
import com.CropAI.CropAI.Service.MqttSubscriberService;
import com.CropAI.CropAI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    @Autowired
    private MqttSubscriberService mqttService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Step 1: Request OTP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody PhoneRequest request) {
        User user = userService.createUser( request.getUsername(),request.getPhone());
        return ResponseEntity.ok("OTP sent to " + request.getPhone());
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest request) {
        String token = userService.verifyOtp(request.getPhone(), request.getOtp());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // Step 3: Fill user details (after OTP verification)
    @PostMapping("/update-details")
    public ResponseEntity<?> updateDetails(@RequestBody UpdateUserRequest request) {
        User updatedUser = userService.updateUserDetails(
                request.getPhone(),
//                request.getName(),
                request.getEmail(),
                request.getAddress()
        );
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToTopics(@AuthenticationPrincipal User user,
                                               @RequestBody List<String> topics) {
        mqttService.subscribeUserToTopics(user.getId(), topics);
        return ResponseEntity.ok("Subscribed to topics: " + topics);
    }

    // Request/Response DTOs
    public static class PhoneRequest {
        private String username;
        private String phone;
        public String getUsername(){return username;}
        public void setUsername(String username) { this.username = username; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }



    }

    public static class OtpRequest {
        private String phone;
        private String otp;
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }

    public static class UpdateUserRequest {
        private String phone;
//        private String name;
        private String email;
        private String address;
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
//        public String getName() { return name; }
//        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class JwtResponse {
        private String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}





//import com.CropAI.CropAI.Entity.User;
//import com.CropAI.CropAI.Service.UserService;
//import com.CropAI.CropAI.Utility.JwtUtil;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(UserService userService, JwtUtil jwtUtil) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody Map<String, String> payload) {
//        String phone = payload.get("phone");
//        String username = payload.get("username");
//
//        User user = userService.createUser(phone, username);
//        return ResponseEntity.ok("OTP sent to " + phone + ". OTP = " + user.getOtp());
//    }
//
//    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
//        String phone = payload.get("phone");
//        String otp = payload.get("otp");
//
//        boolean success = userService.verifyOtp(phone, otp);
//        if (success) {
//            String token = jwtUtil.generateToken(phone);
//            return ResponseEntity.ok(Map.of("message", "OTP verified!", "token", token));
//        } else {
//            return ResponseEntity.status(400).body("Invalid OTP");
//        }
//    }
//
//    @PostMapping("/fill-details")
//    public ResponseEntity<String> fillDetails(@RequestBody Map<String, String> payload) {
//        String phone = payload.get("phone");
//        String name = payload.get("name");
//        String email = payload.get("email");
//        String address = payload.get("address");
//
//        userService.updateUserDetails(phone, name, email, address);
//        return ResponseEntity.ok("User details saved successfully!");
//    }
//}
