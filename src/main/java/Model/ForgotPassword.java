package Model;

import java.sql.Timestamp;
import java.time.*;

public class ForgotPassword {
    private int id;
    private String email;
    private String otp;
    private Timestamp created_at;
    private LocalDateTime expiredAt;

    public ForgotPassword() {}
    public ForgotPassword(int id, String email, String otp, LocalDateTime expiredAt, Timestamp created_at) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.expiredAt = expiredAt;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
