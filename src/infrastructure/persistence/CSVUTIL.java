/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CSVUTIL {
    private static final Logger logger = Logger.getLogger(CSVUTIL.class.getName());

    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(values);
            }
        }
        return records;
    }

    public static void writeCSV(String filePath, List<String[]> records) throws IOException {
        File tempFile = new File(filePath + ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            for (String[] record : records) {
                bw.write(String.join(";", record));
                bw.newLine();
            }
        }
        File originalFile = new File(filePath);
        File backupFile = new File(filePath + ".bak");
        if (originalFile.exists()) {
            if (backupFile.exists()) backupFile.delete();
            originalFile.renameTo(backupFile);
        }
        tempFile.renameTo(originalFile);
    }
}