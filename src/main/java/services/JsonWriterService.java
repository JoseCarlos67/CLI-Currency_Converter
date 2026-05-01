package services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonWriterService {
  private static final String folderPath = System.getProperty("user.home") + File.separator + ".cli-currency-converter";
  private static final String fileName = "apiKey.json";
  private static final File file = new File(folderPath, fileName);

  public static void updateJsonFile(String key) {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      File directory = new File(folderPath);
      if (!directory.exists()) {
        directory.mkdirs();
      }
      objectMapper.writeValue(file, key);
    } catch (IOException e) {
      System.out.println("Error creating file -> " + e.getMessage());
    }
  }
}
