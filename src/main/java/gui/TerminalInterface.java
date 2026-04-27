package gui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import services.ApiCoins;
import services.CurrencyConversionService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;


public class TerminalInterface extends BasicWindow {
  public void createTerminal() {
    DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
    try (Screen screen = terminalFactory.createScreen()) {
      screen.startScreen();

      WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
      Window window = new BasicWindow("CURRENCY CONVERSION");
      window.setHints(Arrays.asList(Hint.CENTERED));


      Panel contentPanel = new Panel(new GridLayout(2));

      contentPanel.addComponent(new Label("Currency of origin"));
      ComboBox comboBox0 = new ComboBox<>(ApiCoins.getSuportedCodes());
      contentPanel.addComponent(comboBox0);

      contentPanel.addComponent(new Label("Destination currency"));
      ComboBox comboBox1 = new ComboBox<>(ApiCoins.getSuportedCodes());
      contentPanel.addComponent(comboBox1);

      contentPanel.addComponent(new Label("Value"));
      TextBox inputValue = new TextBox("100.00");
      contentPanel.addComponent(inputValue);

      contentPanel.addComponent(new EmptySpace());

      Label result = new Label("Result of conversion:");

      Button btnConverter = new Button("Convert", () -> {
        double resultConversion = 0.0;
        inputValue.setText(inputValue.getText().trim());
        if (!inputValue.getText().isEmpty() || !inputValue.getText().equals("")){
          resultConversion = CurrencyConversionService.conversion(comboBox0.getText(), comboBox1.getText(), Double.parseDouble(inputValue.getText()));
        }

        result.setText("Result of conversion: " + resultConversion);
      });
      contentPanel.addComponent(btnConverter);
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
