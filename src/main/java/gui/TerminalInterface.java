package gui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import services.ApiCoins;
import services.CurrencyConversionService;
import services.JsonReaderService;
import services.JsonWriterService;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class TerminalInterface extends BasicWindow {
  private String apiKey = null;

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

      ComboBox comboBox0 = new ComboBox<>("Awaiting API key");
      ComboBox comboBox1 = new ComboBox<>("Awaiting API key");

      Label LabelApiKey = new Label ("API Key:");
      contentPanel.addComponent(LabelApiKey);
      contentPanel.addComponent(new EmptySpace());
      TextBox inputApiKey = new TextBox(readAndValidateKeyFromJson(textGUI, comboBox0, comboBox1));
      inputApiKey.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1));
      contentPanel.addComponent(inputApiKey).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(2));


      Button btnSaveKey = new Button("Save", () ->{
        List<String> supportedCodes = ApiCoins.getSupportedCodes(inputApiKey.getText());

        if (supportedCodes.isEmpty()) {
          MessageDialog.showMessageDialog(textGUI, "API Error", "API KEY NOT FOUND");
        } else {
          comboBox0.clearItems();
          comboBox1.clearItems();
          for (String code : supportedCodes) {
            comboBox0.addItem(code);
            comboBox1.addItem(code);
          }
          MessageDialogButton resultSave = MessageDialog.showMessageDialog(
                  textGUI,
                  "Confirm",
                  "Do you want to save API Key?",
                  MessageDialogButton.Yes,
                  MessageDialogButton.No
          );
          if (resultSave == MessageDialogButton.Yes) {
            JsonWriterService.updateJsonFile(inputApiKey.getText());
          }

          apiKey = inputApiKey.getText();
        }
      }).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(2));
      contentPanel.addComponent(btnSaveKey);

      contentPanel.addComponent(new Label("Currency of origin"));

      contentPanel.addComponent(comboBox0);

      contentPanel.addComponent(new Label("Destination currency"));

      contentPanel.addComponent(comboBox1);

      contentPanel.addComponent(new Label("Value"));
      TextBox inputValue = new TextBox();
      inputValue.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1));
      inputValue.setValidationPattern(Pattern.compile("^[0-9]*\\.?[0-9]*$"));
      contentPanel.addComponent(inputValue);

      Label result = new Label("Result of conversion:");

      Button btnConverter = new Button("Convert", () -> {
        if (!(apiKey == null)){
          Double resultConversion = 0.0;
          inputValue.setText(inputValue.getText().trim());
          if (!inputValue.getText().isEmpty() || !inputValue.getText().equals("")){
            resultConversion = CurrencyConversionService.conversion(inputApiKey.getText(), comboBox0.getText(), comboBox1.getText(), Double.parseDouble(inputValue.getText()));
          }
          if (!(resultConversion == null)) {
            result.setText(String.format("Result of conversion: " + String.format("%.2f", resultConversion)));
            result.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
          } else {

            result.setText("Result of conversion:");
            result.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
            MessageDialog.showMessageDialog(textGUI, "API Error", "API KEY NOT FOUND");
          }
        }
      }).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(2));

      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(btnConverter);
      contentPanel.addComponent(new Separator(Direction.HORIZONTAL)
              .setLayoutData(GridLayout.createLayoutData(
                      GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, false, 2, 1)));
      contentPanel.addComponent(result);
      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(new EmptySpace());
      contentPanel.addComponent(new Button("Exit", ()-> {
        window.close();
        System.exit(0);
      })).setLayoutData(GridLayout.createHorizontallyEndAlignedLayoutData(2));
      window.setComponent(contentPanel);
      textGUI.addWindowAndWait(window);

    } catch (IOException e) {
      MessageDialog.showMessageDialog(getTextGUI(), "Error creating terminal", e.getMessage());
    }
  }

  private String readAndValidateKeyFromJson(WindowBasedTextGUI textGUI, ComboBox comboBox0, ComboBox comboBox1) {
    String apiFromFile = JsonReaderService.apiKeySaved();
    List<String> supportedCodes = ApiCoins.getSupportedCodes(apiFromFile);

    if (supportedCodes.isEmpty()) {
      return "";
    } else {
      comboBox0.clearItems();
      comboBox1.clearItems();
      for (String code : supportedCodes) {
        comboBox0.addItem(code);
        comboBox1.addItem(code);
      }
      return apiKey = apiFromFile;
    }
  }
}
