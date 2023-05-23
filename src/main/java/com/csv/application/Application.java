package com.csv.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        String csvFilePath = "/Users/sofiabodurova/Downloads/csv-reader/src/main/resources/emoloyee.csv";
        Map<String, LocalDate> employeeProjects = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true; // Flag to skip the first line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] data = line.split(",");
                String empID = data[0].trim();
                LocalDate dateFrom = LocalDate.parse(data[2].trim());
                LocalDate dateTo = data[3].trim().equalsIgnoreCase("NULL") ? LocalDate.now() : LocalDate.parse(data[3].trim());

                if (!employeeProjects.containsKey(empID)) {
                    employeeProjects.put(empID, dateTo);
                } else {
                    LocalDate existingDateTo = employeeProjects.get(empID);
                    if (dateTo.isAfter(existingDateTo)) {
                        employeeProjects.put(empID, dateTo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String employee1 = "";
        String employee2 = "";
        long maxDuration = 0;

        for (Map.Entry<String, LocalDate> entry1 : employeeProjects.entrySet()) {
            for (Map.Entry<String, LocalDate> entry2 : employeeProjects.entrySet()) {
                if (!entry1.getKey().equals(entry2.getKey())) {
                    long duration = ChronoUnit.YEARS.between(entry1.getValue(), entry2.getValue());
                    if (duration > maxDuration) {
                        maxDuration = duration;
                        employee1 = entry1.getKey();
                        employee2 = entry2.getKey();
                    }
                }
            }
        }

        System.out.println("Employees with the longest common project duration:");
        System.out.println(employee1 + ", " + employee2 + ", " + maxDuration);
    }

}
