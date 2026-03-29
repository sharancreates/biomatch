package com.biomatch.data;

import com.biomatch.model.Donor;
import com.biomatch.model.Patient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataSeeder {
    // Master arrays to pull random data from
    private static final String[] BLOOD_TYPES = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    private static final String[] HLA_MARKERS = {"HLA-A", "HLA-B", "HLA-C", "HLA-DR", "HLA-DQ"};
    private static final Random random = new Random();

    //generates random Donors
    public static List<Donor> generateDonors(int count) {
        List<Donor> donors = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            String id = "D-" + i;
            String name = "Donor " + i;
            String bloodType = getRandomBloodType();
            Set<String> antigens = getRandomMarkers(3); // Donors get 3 random antigens
            
            donors.add(new Donor(id, name, bloodType, antigens));
        }
        return donors;
    }

    //Generates random Patients
    public static List<Patient> generatePatients(int count) {
        List<Patient> patients = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            String id = "P-" + i;
            String name = "Patient " + i;
            String bloodType = getRandomBloodType();
            Set<String> antibodies = getRandomMarkers(2); // Patients have 2 random antibodies
            int urgency = random.nextInt(10) + 1; // Random urgency 1-10
            
            patients.add(new Patient(id, name, bloodType, antibodies, urgency));
        }
        return patients;
    }

    // --- Helper Methods for Randomization ---

    private static String getRandomBloodType() {
        return BLOOD_TYPES[random.nextInt(BLOOD_TYPES.length)];
    }

    // Generates a mathematical Set of a specific size to test our intersections
    private static Set<String> getRandomMarkers(int numberOfMarkers) {
        Set<String> markers = new HashSet<>();
        
        // Keep adding random markers until the Set reaches the desired size.
        // Because it's a Set, duplicates are automatically ignored!
        while (markers.size() < numberOfMarkers) {
            String randomMarker = HLA_MARKERS[random.nextInt(HLA_MARKERS.length)];
            markers.add(randomMarker);
        }
        return markers;
    }
}