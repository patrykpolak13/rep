package com.example.demo1234.model;

public enum Gender {
    FEMALE("Female"),
    MALE("Male");

    private final String displayValue;

    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override public String toString() { return displayValue; }
}
