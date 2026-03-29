package com.biomatch.main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.biomatch.model.Donor;
import com.biomatch.model.Patient;
import com.biomatch.logic.CompatibilityEngine;
import com.biomatch.logic.WaitlistSorter;
import com.biomatch.data.DataSeeder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class BioMatchServer {

    public static void main(String[] args) throws IOException {
        // 1. Initialize the Native Java HTTP Server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 2. Create the API endpoint route
        server.createContext("/api/match", new MatchMatrixHandler());

        // 3. Start the server
        server.setExecutor(null); 
        server.start();
        System.out.println("====== BioMatch Math Engine Active ======");
        System.out.println("Listening for React frontend on http://localhost:8080/api/match");
    }

    // --- The Request Handler ---
    static class MatchMatrixHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
            // SECURITY: Allow your React app (running on a different port) to read this data
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            // STEP 1: Generate Test Data
            List<Donor> donors = DataSeeder.generateDonors(5); // 5 random donors
            List<Patient> patients = DataSeeder.generatePatients(5); // 5 random patients
            
            // STEP 2: Apply Poset Math (Sort the waitlist by urgency)
            patients.sort(new WaitlistSorter());

            // STEP 3: Generate the Bipartite Graph / Adjacency Matrix
            int[][] matrix = new int[donors.size()][patients.size()];
            
            for (int i = 0; i < donors.size(); i++) {
                for (int j = 0; j < patients.size(); j++) {
                    Donor d = donors.get(i);
                    Patient p = patients.get(j);
                    
                    // Apply Compound Propositional Logic & Set Intersection
                    boolean isSafe = CompatibilityEngine.isBloodCompatible(d.getBloodType(), p.getBloodType()) 
                                  && CompatibilityEngine.isTissueCompatible(d.getHlaMarkers(), p.getHlaMarkers());
                    
                    matrix[i][j] = isSafe ? 1 : 0;
                }
            }

            // STEP 4: Build the JSON Response manually (No external libraries!)
            String jsonResponse = buildJson(donors, patients, matrix);

            // STEP 5: Send the JSON back to the browser/React
            byte[] responseBytes = jsonResponse.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }

        // --- Helper Method: Manual JSON Serialization ---
        // This builds the exact string structure React needs to render the dashboard.
        private String buildJson(List<Donor> donors, List<Patient> patients, int[][] matrix) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");

            // 1. Add Donors Array
            sb.append("  \"donors\": [");
            for (int i = 0; i < donors.size(); i++) {
                sb.append("\"").append(donors.get(i).getId()).append(" (").append(donors.get(i).getBloodType()).append(")\"");
                if (i < donors.size() - 1) sb.append(", ");
            }
            sb.append("],\n");

            // 2. Add Patients Array (showing their sorted Urgency!)
            sb.append("  \"patients\": [");
            for (int j = 0; j < patients.size(); j++) {
                sb.append("\"").append(patients.get(j).getId()).append(" (Urg: ").append(patients.get(j).getUrgencyLevel()).append(")\"");
                if (j < patients.size() - 1) sb.append(", ");
            }
            sb.append("],\n");

            // 3. Add the Matrix Data
            sb.append("  \"matrix\": [\n");
            for (int i = 0; i < matrix.length; i++) {
                sb.append("    [");
                for (int j = 0; j < matrix[i].length; j++) {
                    sb.append(matrix[i][j]);
                    if (j < matrix[i].length - 1) sb.append(", ");
                }
                sb.append("]");
                if (i < matrix.length - 1) sb.append(",\n");
                else sb.append("\n");
            }
            sb.append("  ]\n");

            sb.append("}");
            return sb.toString();
        }
    }
}