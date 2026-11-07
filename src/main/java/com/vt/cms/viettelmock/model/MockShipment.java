package com.vt.cms.viettelmock.model;

import java.time.LocalDateTime;

public class MockShipment {
    private String orderNumber;
    private String orderReference;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Long codAmount;
    private Integer weight;
    private Integer status;
    private String statusName;
    private String note;
    private String locationCurrently;
    private LocalDateTime updatedAt;

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderReference() { return orderReference; }
    public void setOrderReference(String orderReference) { this.orderReference = orderReference; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    public Long getCodAmount() { return codAmount; }
    public void setCodAmount(Long codAmount) { this.codAmount = codAmount; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getLocationCurrently() { return locationCurrently; }
    public void setLocationCurrently(String locationCurrently) { this.locationCurrently = locationCurrently; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}