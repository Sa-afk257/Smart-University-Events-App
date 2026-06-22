package com.example.universityevents_1221618.models;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String major;
    private String phone;
    private String profilePicUri;
    private String role; // "USER" or "ADMIN"

    public User() {}

    public User(int id, String email, String firstName, String lastName, String password, String gender, String major, String phone, String profilePicUri, String role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.major = major;
        this.phone = phone;
        this.profilePicUri = profilePicUri;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProfilePicUri() { return profilePicUri; }
    public void setProfilePicUri(String profilePicUri) { this.profilePicUri = profilePicUri; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}

