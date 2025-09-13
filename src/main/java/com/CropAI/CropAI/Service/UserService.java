package com.CropAI.CropAI.Service;

import com.CropAI.CropAI.Entity.User;
import com.CropAI.CropAI.Repositories.UserRepository;
import com.CropAI.CropAI.Utility.JwtUtil;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${twilio.account-sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String TWILIO_AUTH_TOKEN;

    @Value("${twilio.phone-number}")
    private String TWILIO_PHONE_NUMBER;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    // Step 1: Create user and send OTP
    public User createUser(String username, String phone) {
        String otp = generateOtp();

        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    User u = new User();
                    u.setName(username);
                    u.setPhone(phone);
                    return u;
                });

        user.setOtp(otp);
        user.setVerified(false);
        User savedUser = userRepository.save(user);

        // Send OTP via Twilio
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(phone),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                "Your OTP is: " + otp
        ).create();

        return savedUser;
    }

    // Step 2: Verify OTP and return JWT
    public String verifyOtp(String phone, String otp) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getOtp().equals(otp)) {
                user.setVerified(true);
                userRepository.save(user);

                // Generate JWT token for verified user
                return jwtUtil.generateToken(user.getPhone());
            }
        }
        throw new RuntimeException("Invalid OTP or phone number");
    }

    // Step 3: Update user details
    public User updateUserDetails(String phone,String email, String address) {
        return userRepository.findByPhone(phone)
                .map(user -> {
                    if (!user.isVerified()) throw new RuntimeException("User not verified yet");
//                    user.setName(name);
                    user.setEmail(email);
                    user.setAddress(address);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
}


//
//import com.CropAI.CropAI.Entity.User;
//import com.CropAI.CropAI.Repositories.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Random;
//
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    private String generateOtp() {
//        return String.valueOf(100000 + new Random().nextInt(900000));
//    }
//
//    // Step 1: signup
//    public User createUser(String phone, String username) {
//        String otp = generateOtp();
//        User user = new User();
//        user.setPhone(phone);
//        user.setUsername(username);
//        user.setOtp(otp);
//        user.setVerified(false);
//        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
//        return userRepository.save(user);
//    }
//
//    // Step 2: verify OTP
//    public boolean verifyOtp(String phone, String otp) {
//        return userRepository.findByPhone(phone)
//                .map(user -> {
//                    if (user.getOtp().equals(otp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
//                        user.setVerified(true);
//                        userRepository.save(user);
//                        return true;
//                    }
//                    return false;
//                }).orElse(false);
//    }
//
//    // Step 3: fill details
//    public User updateUserDetails(String phone, String name, String email, String address) {
//        return userRepository.findByPhone(phone)
//                .map(user -> {
//                    if (!user.isVerified()) {
//                        throw new RuntimeException("OTP not verified yet");
//                    }
//                    user.setName(name);
//                    user.setEmail(email);
//                    user.setAddress(address);
//                    return userRepository.save(user);
//                }).orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    public Optional<User> findByPhone(String phone) {
//        return userRepository.findByPhone(phone);
//    }
//}
//
