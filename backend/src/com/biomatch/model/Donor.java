package com.biomatch.model;

import java.util.Set;

public class Donor extends Person {
    
    public Donor(String id, String name, String bloodType, Set<String> antigens) {
        
        super(id, name, bloodType, antigens);
    }
}