package application;

import gui.TerminalInterface;
import services.CurrencyConversionService;

import java.io.IOException;

public class Program {
  public static void main(String[] args)  {
    TerminalInterface terminalInterface = new TerminalInterface();
    terminalInterface.createTerminal();
  }
}
