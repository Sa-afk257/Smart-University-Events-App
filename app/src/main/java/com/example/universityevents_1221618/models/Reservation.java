package com.example.universityevents_1221618.models;

public class Reservation {
    private int id;
    private int userId;
    private Event event;
    private String reservationDate;
    private int quantity;
    private String reservationType;
    private String status;

    public Reservation(int id, int userId, Event event, String reservationDate,
                       int quantity, String reservationType, String status) {
        this.id = id;
        this.userId = userId;
        this.event = event;
        this.reservationDate = reservationDate;
        this.quantity = quantity;
        this.reservationType = reservationType;
        this.status = status;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public Event getEvent() { return event; }
    public String getReservationDate() { return reservationDate; }
    public int getQuantity() { return quantity; }
    public String getReservationType() { return reservationType; }
    public String getStatus() { return status; }
}