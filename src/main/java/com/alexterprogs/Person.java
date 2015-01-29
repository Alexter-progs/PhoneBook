package com.alexterprogs;

public class Person {

    private String fullName;
    private String phoneNumber;
    private String jobPosition;
    private String roomNumber;
    private String department;

    public Person(String jobPosition, String fullName, String phoneNumber, String roomNumber, String department){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.jobPosition = jobPosition;
        this.roomNumber = roomNumber;
        this.department = department;
    }

    public String getFullName () {
        return fullName;
    }

    public String getPhoneNumber () {
        return phoneNumber;
    }

    public String getJobPosition () {
        return jobPosition;
    }

    public String getRoomNumber () {
        return roomNumber;
    }

    public String getDepartment () {
        return department;
    }
}
