package com.biomatch.logic;

import com.biomatch.model.Patient;
import java.util.HashSet;
import java.util.Set;

public class CompatibilityEngine {
     //propositional Logic : strict boolean rules for blood compatibility.

/**
     * TOPIC 10: Compound Propositional Logic
     * Evaluates ABO compatibility AND Rh compatibility.
     * Formula: (ABO_Safe) ∧ (Rh_Safe)
     */
    public static boolean isBloodCompatible(String donorBlood, String patientBlood) {
        
        // 1. Parse the strings (e.g., separate "AB+" into "AB" and "+")
        String donorABO = donorBlood.substring(0, donorBlood.length() - 1);
        char donorRh = donorBlood.charAt(donorBlood.length() - 1);
        
        String patientABO = patientBlood.substring(0, patientBlood.length() - 1);
        char patientRh = patientBlood.charAt(patientBlood.length() - 1);

        // --- PROPOSITION P: Is ABO Compatible? ---
        boolean isAboSafe = false;
        if (donorABO.equals("O")) isAboSafe = true;                  // O is universal ABO donor
        else if (patientABO.equals("AB")) isAboSafe = true;          // AB is universal ABO recipient
        else isAboSafe = donorABO.equals(patientABO);                // Exact match

        // --- PROPOSITION Q: Is Rh Factor Compatible? ---
        boolean isRhSafe = false;
        if (donorRh == '-') isRhSafe = true;                         // Negative can give to anyone
        else if (patientRh == '+') isRhSafe = true;                  // Positive can only give to Positive

        // --- FINAL LOGIC: P ∧ Q ---
        // Both conditions must be strictly true for a safe transplant.
        return isAboSafe && isRhSafe;
    }

    /**
     * TOPIC 1: Set Theory (Intersection)
     * Proves biological safety by ensuring the intersection of donor antigens 
     * and patient antibodies is an empty set (∅).
     */
    public static boolean isTissueCompatible(Set<String> donorAntigens, Set<String> patientAntibodies) {
        // 1. Create a copy of the donor's set so we don't accidentally delete their actual data
        Set<String> conflictSet = new HashSet<>(donorAntigens);
        
        // 2. Perform the Mathematical Intersection: A ∩ B
        conflictSet.retainAll(patientAntibodies); 
        
        // 3. Evaluate the result. If the set is empty, there are no conflicts.
        return conflictSet.isEmpty();
    }

    /**
     * TOPIC 8: Partial Ordering (Posets)
     * Creates a mathematical hierarchy to sort the waitlist queue.
     * Returns a positive number if p1 is lower priority, negative if p1 is higher.
     */
    public static int comparePatients(Patient p1, Patient p2) {
        // We sort in descending order (a priority of 10 comes before a priority of 1)
        return Integer.compare(p2.getUrgencyLevel(), p1.getUrgencyLevel());
    }
}