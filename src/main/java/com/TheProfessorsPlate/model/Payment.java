package com.TheProfessorsPlate.model;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    private int paymentId;
    private Date paymentDate;
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal paymentAmount;
    
    public Payment() {
    }
    
    public Payment(int paymentId, Date paymentDate, String paymentMethod, String paymentStatus, BigDecimal paymentAmount) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
    }

    // Getters and setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString() {
        return "Payment [paymentId=" + paymentId + ", paymentDate=" + paymentDate + ", paymentMethod=" + paymentMethod
                + ", paymentStatus=" + paymentStatus + ", paymentAmount=" + paymentAmount + "]";
    }
}