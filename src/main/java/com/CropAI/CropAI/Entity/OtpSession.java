package com.CropAI.CropAI.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "otp_sessions")
public class OtpSession {

    @Id
    private String id;

    private String phone;
    private String otpCode;
    private Instant otpExpiry;
}
