/*
* File: Main.java
* Author: Claire Stovall
* Date: April 8, 2018
* Purpose: This program is the main class for the program that parses,
* using recursive descent, a GUI definition language defined in an input file
* and generates the GUI that it defines.
* Inspired by Module 1: Formal Syntax and Semantics: C Program Formatter Written
* in Java
*/

import java.io.*;

public class Main {

  private static final BufferedReader stdin =
    new BufferedReader(new InputStreamReader(System.in));

  public static void main(String[] args) throws IOException {
    // Get the file name
    System.out.print("Enter file name (including .txt extension): ");
    String fileName = stdin.readLine();

    // Instantiate the gui, lexer, and parser objects
    BuildGUI gui = new BuildGUI();
    try {
      Lexer lexer = new Lexer(fileName, gui);
      Parser parser = new Parser(lexer, gui);

      // Do the parsing
      if (parser.gui()) {
        // Display the GUI
        gui.display();
      }
      // Close the input file
      lexer.close();
    } catch (FileNotFoundException | IncorrectInputException ex) {
      System.out.println(ex.toString());
      System.exit(0);
    }
  }
}
