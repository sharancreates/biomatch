package com.biomatch.model;

import java.util.HashSet;
import java.util.Set;

public abstract class Person {
    protected String id;
    protected String name;
    protected String bloodType;
    
    //a set cannot contain duplicate elements: no duplicate antigens
    protected Set<String> hlaMarkers; 

    public Person(String id, String name, String bloodType, Set<String> hlaMarkers) {
        this.id = id;
        this.name = name;
        this.bloodType = bloodType;
        this.hlaMarkers = new HashSet<>(hlaMarkers);  
    }

    // Getters so our Logic Engine can read the data later
    public String getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public String getBloodType() { 
        return bloodType; 
    }
    public Set<String> getHlaMarkers() { 
        return hlaMarkers; 
    }
}