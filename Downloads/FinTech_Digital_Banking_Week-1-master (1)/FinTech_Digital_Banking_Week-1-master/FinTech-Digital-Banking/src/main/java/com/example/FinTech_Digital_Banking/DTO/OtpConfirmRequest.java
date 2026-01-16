
package com.example.FinTech_Digital_Banking.DTO;


public class OtpConfirmRequest {
    private String transactionId;
    private String otp;

    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
}
