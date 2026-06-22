package com.example.universityevents_1221618.models;

public class Reservation {
    private int id;
    private int userId;
    private Event Event;
    private String reservationDate;

    public Reservation(int id, int userId, Event Event, String reservationDate) {
        this.id = id;
        this.userId = userId;
        this.Event = Event;
        this.reservationDate = reservationDate;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public Event getEvent() { return Event; }
    public String getReservationDate() { return reservationDate; }
}
