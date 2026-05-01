package services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.datatransfer.FlavorEvent;
import java.io.File;
import java.io.IOException;

public class JsonReaderService {
  private static final String folderPath = System.getProperty("user.home") + File.separator + ".cli-currency-converter";
  private static final String fileName = "apiKey.json";
  private static final File file = new File(folderPath, fileName);

  public static String apiKeySaved() {
    File directory = new File(folderPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    ObjectMapper objectMapper = new ObjectMapper();

    if (file.exists()) {
      try {
        return objectMapper.readValue(file, String.class);
      } catch (IOException e) {
        return null;
      }
    } else {
      return "";
    }
  }
}
