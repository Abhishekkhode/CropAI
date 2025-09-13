package com.CropAI.CropAI.Service;


import com.CropAI.CropAI.Entity.OtpSession;
import com.CropAI.CropAI.Repositories.OtpSessionRepository;
import com.CropAI.CropAI.Service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpSessionRepository otpRepo;

    public String generateOtp(String phone) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6 digit
        Instant expiry = Instant.now().plus(5, ChronoUnit.MINUTES);

        otpRepo.deleteByPhone(phone); // clear old session
        otpRepo.save(OtpSession.builder()
                .phone(phone)
                .otpCode(otp)
                .otpExpiry(expiry)
                .build());
        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        Optional<OtpSession> sessionOpt = otpRepo.findByPhone(phone);
        if (sessionOpt.isPresent()) {
            OtpSession session = sessionOpt.get();
            if (session.getOtpCode().equals(otp) &&
                    session.getOtpExpiry().isAfter(Instant.now())) {
                otpRepo.deleteByPhone(phone); // cleanup
                return true;
            }
        }
        return false;
    }
}
