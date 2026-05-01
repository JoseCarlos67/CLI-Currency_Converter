package gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import services.ApiCoins;
import services.CurrencyConversionService;
import services.JsonReaderService;
import services.JsonWriterService;

import java.io.IOException;
import java.util.Arrays;


public class TerminalInterface extends BasicWindow {
  public void createTerminal() {
    DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
    try (Screen screen = terminalFactory.createScreen()) {
      screen.startScreen();

      WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
      Theme customTheme = SimpleTheme.makeTheme(
              true,
              TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK,
              TextColor.ANSI.BLACK, TextColor.ANSI.RED_BRIGHT,
              TextColor.ANSI.BLACK, TextColor.ANSI.RED_BRIGHT,
              TextColor.ANSI.BLACK
      );
      textGUI.setTheme(customTheme);
      Window window = new BasicWindow("CURRENCY CONVERSION");
      window.setHints(Arrays.asList(Hint.CENTERED));

      GridLayout grid = new GridLayout(2);
      grid.setHorizontalSpacing(2);
      Panel contentPanel = new Panel(grid);

      Label apiKey = new Label ("API Key:");
      contentPanel.addComponent(apiKey);
      contentPanel.addComponent(new EmptySpace());
      TextBox inputApiKey = new TextBox(JsonReaderService.apiKeySaved());
      contentPanel.addComponent(inputApiKey);

      Button btnSaveKey = new Button("Save", () ->{
        JsonWriterService.updateJsonFile(inputApiKey.getText());
      });
      contentPanel.addComponent(btnSaveKey);

      contentPanel.addComponent(new Label("Currency of origin"));
      ComboBox comboBox0 = new ComboBox<>(ApiCoins.getSuportedCodes());
      contentPanel.addComponent(comboBox0);

      contentPanel.addComponent(new Label("Destination currency"));
      ComboBox comboBox1 = new ComboBox<>(ApiCoins.getSuportedCodes());
      contentPanel.addComponent(comboBox1);

      contentPanel.addComponent(new Label("Value"));
      TextBox inputValue = new TextBox("100.00");
      contentPanel.addComponent(inputValue);

      Label result = new Label("Result of conversion:");

      Button btnConverter = new Button("Convert", () -> {
        Double resultConversion = 0.0;
        inputValue.setText(inputValue.getText().trim());
        if (!inputValue.getText().isEmpty() || !inputValue.getText().equals("")){
          resultConversion = CurrencyConversionService.conversion(inputApiKey.getText(), comboBox0.getText(), comboBox1.getText(), Double.parseDouble(inputValue.getText()));
        }
        if (!(resultConversion == null)) {
          result.setText(String.format("Result of conversion: " + String.format("%.2f", resultConversion)));
          result.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        } else {
          result.setText("Result of conversion: API KEY NOT FOUND IN EXCHANGERATE-API");
          result.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        }
      });

      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(btnConverter);
      contentPanel.addComponent(new Separator(Direction.HORIZONTAL)
              .setLayoutData(GridLayout.createLayoutData(
                      GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false, 2, 1)));
      contentPanel.addComponent(result);
      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(new Button("Fechar", ()-> {
        window.close();
        System.exit(0);
      }));
      window.setComponent(contentPanel);
      textGUI.addWindowAndWait(window);

    } catch (IOException e) {
      System.out.println("Error: Error creating terminal -> " + e.getMessage());
    }
  }
}
