/*
* File: IncorrectInputException.java
* Author: Claire Stovall
* Date: April 8, 2018
* Purpose: This program is the IncorrectInputException class for the program
* that parses, using recursive descent, a GUI definition language defined in an
* input file and generates the GUI that it defines. The class defines a custom
* exception for incorrect syntax in the input file.
*/

public class IncorrectInputException extends Exception {

  private String errorLocation;
  private Lexer lexer;

  // Constructor
  public IncorrectInputException(String errorLocation, Lexer lexer) {
    this.errorLocation = errorLocation;
    this.lexer = lexer;
  }

  // To String method
  public String toString(){
   return ("Incorrect syntax error: " + errorLocation + " not found at \""
      + lexer.getLastLexeme() + " " + lexer.getCurrentLexeme()
      + "\" near line " + lexer.getLineCounter());
  }
}
