package com.example.fitfurlife;

public class accGyro {
    private int id;
    private float acceleration;
    private float gyroscope;
    private float time;

    public accGyro(int id, float acceleration, float gyroscope, float time)
    {
        this.id = id;
        this.acceleration = acceleration;
        this.gyroscope = gyroscope;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(float gyroscope) {
        this.gyroscope = gyroscope;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}