/*
* File: Lexer.java
* Author: Claire Stovall
* Date: April 8, 2018
* Purpose: This program is the lexer class for the program that parses,
* using recursive descent, a GUI definition language defined in an input file
* and generates the GUI that it defines. The class returns tokens from the
* input file upon each method call.
* Inspired by Module 1: Formal Syntax and Semantics: C Program Formatter Written
* in Java
*/

import java.io.*;

public class Lexer {

  private char character;
  private int i;
  private String line = "";
  private BufferedReader file;
  private BuildGUI gui;
  private Token currentToken;
  private Token lastToken;
  private String currentLexeme;
  private String lastLexeme;
  private boolean inQuotes;
  private static int lineCounter;

  enum Token {NOT_FOUND, END_OF_FILE, WINDOW, LAYOUT, FLOW, GRID, BUTTON,
      GROUP, LABEL, PANEL, TEXTFIELD, RADIO, QUOTATION_MARK, LEFT_PARENTHESIS,
      RIGHT_PARENTHESIS, SEMICOLON, COLON, COMMA, PERIOD, END, STRING, NUMBER};

  // Constructor
  public Lexer(String fileName, BuildGUI gui) throws FileNotFoundException {
    file = new BufferedReader(new FileReader(fileName));
    lineCounter = 0;
    character = nextChar();
    currentLexeme = "";
    inQuotes = false;
    lastToken = Token.NOT_FOUND;
    this.gui = gui;
  }

  // Method to close input file
  public void close() throws IOException {
    file.close();
  }

  // Method to return the next token in the input file
  // and display the previous token.
  public Token getNextToken() {
    boolean flag = false;
    lastToken = currentToken;
    lastLexeme = currentLexeme;
    currentLexeme = "";
    while (character != 0 && Character.isWhitespace(character)
        && !inQuotes) {
      character = nextChar();
    }
    if (Character.isUpperCase(character) && !inQuotes) {
      while (Character.isLetter(character)
          && !testPunctuation(Character.toString(character))) {
        currentLexeme += character;
        character = nextChar();
      }
      currentToken = testType(currentLexeme);
    } else if (inQuotes && (Character.isLetter(character)
        || Character.isWhitespace(character)
        || Character.isDigit(character)
        || testSpecialCharacter(Character.toString(character)))) {
      while ((Character.isLetter(character)
          || Character.isWhitespace(character)
          || Character.isDigit(character)
          || testSpecialCharacter(Character.toString(character)))
          && !testStringPunctuation(Character.toString(character))) {
        currentLexeme += character;
        character = nextChar();
      }
      currentToken = Token.STRING;
    } else if (Character.isDigit(character) && !inQuotes) {
      while (Character.isDigit(character)
          && !testNumberPunctuation(Character.toString(character))) {
        currentLexeme += character;
        character = nextChar();
      }
      currentToken = Token.NUMBER;
    } else if (testPunctuation(Character.toString(character))) {
      currentLexeme = Character.toString(character);
      currentToken = isPunctuation(currentLexeme);
      if (currentToken == Token.QUOTATION_MARK
          && lastToken == Token.QUOTATION_MARK) {
        currentToken = Token.STRING;
        currentLexeme = "";
        character = '\"';
        flag = true;
      }
      if (currentToken == Token.QUOTATION_MARK && !inQuotes) {
        inQuotes = true;
      } else if (currentToken == Token.QUOTATION_MARK && inQuotes) {
        inQuotes = false;
      }
      if (!flag) {
        character = nextChar();
      }
    } else if (character == Character.MIN_VALUE) {
      currentToken = Token.END_OF_FILE;
    } else {
      currentLexeme = Character.toString(character);
      currentToken = Token.NOT_FOUND;
    }
    return currentToken;
  }

  //  Returns the lexeme corresponding to the last token.
  public String getLastLexeme() {
    return lastLexeme;
  }

  //  Returns the lexeme corresponding to the current token.
  public String getCurrentLexeme() {
    return currentLexeme;
  }

  //  Returns the next character in the input.
  private char nextChar() {
    try {
      if (line == null) {
        return Character.MIN_VALUE;
      }
      if (i == line.length()) {
        line = file.readLine();
        lineCounter++;
        i = 0;
        return '\n';
      }
      return line.charAt(i++);
    } catch (IOException exception) {
      return Character.MIN_VALUE;
    }
  }

  public int getLineCounter() {
    return lineCounter;
  }

  // Methods to test for punctuation
  private boolean testPunctuation(String lexeme) {
    if (lexeme.matches("[;\",).:(]")) {
      return true;
    }
    return false;
  }

  private boolean testSpecialCharacter(String lexeme) {
    if (lexeme.matches("[!@#$%^&*?><,.\';()-_+=/{}|~`:]")) {
      return true;
    }
    return false;
  }

  private boolean testStringPunctuation(String lexeme) {
    if (lexeme.matches("[\"]")) {
      return true;
    }
    return false;
  }

  private boolean testNumberPunctuation(String lexeme) {
    if (lexeme.matches("[;,)]")) {
      return true;
    }
    return false;
  }

  // Method to determine type of punctuation
  private Token isPunctuation(String lexeme) {
    switch (lexeme.charAt(0)) {
      case '\"':
        return Token.QUOTATION_MARK;
      case '\'':
        return Token.QUOTATION_MARK;
      case '(':
        return Token.LEFT_PARENTHESIS;
      case ')':
        return Token.RIGHT_PARENTHESIS;
      case ';':
        return Token.SEMICOLON;
      case ':':
        return Token.COLON;
      case ',':
        return Token.COMMA;
      case '.':
        return Token.PERIOD;
    }
    return Token.NOT_FOUND;
  }

  // Method to return token type when lexeme begins with a capital letter and
  // isn't in punctuation.
  private Token testType(String lexeme) {
    switch (lexeme.charAt(0)) {
      case 'W':
        if (lexeme.equals("Window")) {
          return Token.WINDOW;
        }
        break;
      case 'E':
        if (lexeme.equals("End")) {
            return Token.END;
        }
        break;
      case 'L':
        if (lexeme.equals("Label")) {
          return Token.LABEL;
        } else if (lexeme.equals("Layout")) {
          return Token.LAYOUT;
        }
        break;
      case 'F':
        if (lexeme.equals("Flow")) {
          return Token.FLOW;
        }
        break;
      case 'G':
        if (lexeme.equals("Grid")) {
          return Token.GRID;
        } else if (lexeme.equals("Group")) {
          return Token.GROUP;
        }
        break;
      case 'B':
        if (lexeme.equals("Button")) {
          return Token.BUTTON;
        }
        break;
      case 'P':
        if (lexeme.equals("Panel")) {
          return Token.PANEL;
        }
        break;
      case 'T':
        if (lexeme.equals("Textfield")) {
          return Token.TEXTFIELD;
        }
        break;
      case 'R':
        if (lexeme.equals("Radio")) {
          return Token.RADIO;
        }
        break;
    }
    return Token.NOT_FOUND;
  }
}
