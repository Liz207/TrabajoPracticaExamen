/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.persistence;


import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JSONUtil {
    private static final Logger logger = Logger.getLogger(JSONUtil.class.getName());

    public static String serialize(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) {
            return "\"" + ((String) obj).replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof LocalDateTime) {
            return "\"" + ((LocalDateTime) obj).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"";
        }
        // Aquí se debería implementar la serialización recursiva para objetos complejos.
        // Para simplificar, devolvemos una representación básica.
        logger.warning("Serialización JSON simplificada para objeto: " + obj.getClass().getName());
        return "\"" + obj.toString().replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    public static void writeJSON(String filePath, List<?> objects) throws IOException {
        File tempFile = new File(filePath + ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            bw.write("[");
            for (int i = 0; i < objects.size(); i++) {
                if (i > 0) bw.write(",");
                bw.write(serialize(objects.get(i)));
            }
            bw.write("]");
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