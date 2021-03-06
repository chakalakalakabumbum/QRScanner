package com.example.newpc.qrcode.Entities;

import java.util.Date;

/**
 * Booking Entity
 *
 */
public class Booking {
    private Integer bookingId;
    private Integer accountId;
    private Integer parkingLotId;
    private Date bookingTime;
    private Date timeStart;
    private Date timeEnd;
    private String tokenInput;
    private String tokenOutput;
    private String parkingLotName;
    private float moneyToPay;
    private float price;
    private String plateNumber;
    /**
     * Booking Status
     * None: Default status when booking record had created
     * Booked: Booking status when driver go in and waiting them go out
     * Finish: Booking status when driver payment successful.
     */
    private String bookingStatus;

    private String urlApiCheckIn;
    private String urlApiCheckOut;

    public Booking() {
    }

    public Booking(Integer bookingId, Integer accountId, Integer parkingLotId, Date bookingTime, Date timeStart, Date timeEnd, String tokenInput, String tokenOutput, String parkingLotName, float moneyToPay, float price, String plateNumber, String bookingStatus, String urlApiCheckIn, String urlApiCheckOut) {
        this.bookingId = bookingId;
        this.accountId = accountId;
        this.parkingLotId = parkingLotId;
        this.bookingTime = bookingTime;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.tokenInput = tokenInput;
        this.tokenOutput = tokenOutput;
        this.parkingLotName = parkingLotName;
        this.moneyToPay = moneyToPay;
        this.price = price;
        this.plateNumber = plateNumber;
        this.bookingStatus = bookingStatus;
        this.urlApiCheckIn = urlApiCheckIn;
        this.urlApiCheckOut = urlApiCheckOut;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public float getMoneyToPay() {
        return moneyToPay;
    }

    public void setMoneyToPay(float moneyToPay) {
        this.moneyToPay = moneyToPay;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTokenInput() {
        return tokenInput;
    }

    public void setTokenInput(String tokenInput) {
        this.tokenInput = tokenInput;
    }

    public String getTokenOutput() {
        return tokenOutput;
    }

    public void setTokenOutput(String tokenOutput) {
        this.tokenOutput = tokenOutput;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getUrlApiCheckIn() {
        return urlApiCheckIn;
    }

    public void setUrlApiCheckIn(String urlApiCheckIn) {
        this.urlApiCheckIn = urlApiCheckIn;
    }

    public String getUrlApiCheckOut() {
        return urlApiCheckOut;
    }

    public void setUrlApiCheckOut(String urlApiCheckOut) {
        this.urlApiCheckOut = urlApiCheckOut;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
