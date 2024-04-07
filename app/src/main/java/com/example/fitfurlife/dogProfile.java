package com.example.fitfurlife;

public class dogProfile {
    private int id;
    private String dogName;
    private int dogAge;
    private float dogMass;
    private String dogRace;

    public dogProfile(int id, String dogName, int dogAge, float dogMass, String dogRace){
        this.id = id;
        this.dogName = dogName;
        this.dogAge = dogAge;
        this.dogMass = dogMass;
        this.dogRace = dogRace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public int getDogAge() {
        return dogAge;
    }

    public void setDogAge(int dogAge) {
        this.dogAge = dogAge;
    }

    public float getDogMass() {
        return dogMass;
    }

    public void setDogMass(float dogMass) {
        this.dogMass = dogMass;
    }

    public String getDogRace() {
        return dogRace;
    }

    public void setDogRace(String dogRace) {
        this.dogRace = dogRace;
    }


}