package com.biomatch.model;

import java.util.Set;

public class Patient extends Person {
    
    private int urgencyLevel; // Used for waitlist sorting (1-10) poset implementation

    public Patient(String id, String name, String bloodType, Set<String> antibodies, int urgencyLevel) {
        super(id, name, bloodType, antibodies);
        this.urgencyLevel = urgencyLevel;
    }

    public int getUrgencyLevel() { 
        return urgencyLevel; 
    }
}