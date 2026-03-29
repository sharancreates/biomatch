package com.biomatch.logic;

import com.biomatch.model.Patient;
import java.util.Comparator;

/**
 * TOPIC 8: Partial Ordering (Posets)
 * This class mathematically defines the "Greater Than or Equal To" (≥) relation 
 * for our set of patients to create a strict priority queue.
 */
public class WaitlistSorter implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        // We want descending order: A patient with Urgency 10 must come BEFORE Urgency 1.
        // Integer.compare normally sorts ascending (1, 2, 3). 
        // By flipping p2 and p1, we force descending order (3, 2, 1).
        
        int urgencyComparison = Integer.compare(p2.getUrgencyLevel(), p1.getUrgencyLevel());
        
        // --- Advanced Poset Logic (Tie-Breaker) ---
        // If two patients have the EXACT same urgency (e.g., both are level 8),
        // we use their ID as a secondary sorting rule so the order is always mathematically predictable.
        if (urgencyComparison == 0) {
            return p1.getId().compareTo(p2.getId());
        }
        
        return urgencyComparison;
    }
}