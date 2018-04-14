/*
* File: Parser.java
* Author: Claire Stovall
* Date: April 8, 2018
* Purpose: This program is the parser class for the program that parses,
* using recursive descent, a GUI definition language defined in an input file
* and generates the GUI that it defines. The class uses recursive descent
* parsing to parse the input and build the GUI.
*
* Inspired by Module 1: Formal Syntax and Semantics: C Program Formatter Written
* in Java
*/

public class Parser {
    private Lexer lexer;
    private BuildGUI gui;
    private Lexer.Token token;

    // Constructor
    public Parser(Lexer lexer, BuildGUI gui) {
      this.lexer = lexer;
      this.gui = gui;
    }

    // Elements are added to the GUI
    public boolean gui() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.WINDOW) {
        token = lexer.getNextToken();
        if (token == Lexer.Token.QUOTATION_MARK) {
          token = lexer.getNextToken();
          if (token == Lexer.Token.STRING) {
            gui.setTitle(lexer.getCurrentLexeme());
            token = lexer.getNextToken();
            if (token == Lexer.Token.QUOTATION_MARK) {
              token = lexer.getNextToken();
              if (token == Lexer.Token.LEFT_PARENTHESIS) {
                token = lexer.getNextToken();
                if (token == Lexer.Token.NUMBER) {
                  String width = lexer.getCurrentLexeme();
                  token = lexer.getNextToken();
                  if (token == Lexer.Token.COMMA) {
                    token = lexer.getNextToken();
                    if (token == Lexer.Token.NUMBER) {
                      gui.setDimensions(Integer.parseInt(width),
                          Integer.parseInt(lexer.getCurrentLexeme()));
                      token = lexer.getNextToken();
                      if (token == Lexer.Token.RIGHT_PARENTHESIS) {
                        token = lexer.getNextToken();
                        if (token == Lexer.Token.LAYOUT) {
                          if (layout()) {
                            if (token == Lexer.Token.BUTTON ||
                                token == Lexer.Token.GROUP ||
                                token == Lexer.Token.LABEL ||
                                token == Lexer.Token.PANEL ||
                                token == Lexer.Token.TEXTFIELD) {
                              if (widgets()) {
                                if (token == Lexer.Token.PERIOD) {
                                  token = lexer.getNextToken();
                                  if (token == Lexer.Token.END_OF_FILE) {
                                    return true;
                                  } else {
                                    throw new IncorrectInputException("End"
                                        + " of file", lexer);
                                  }
                                } else {
                                  throw new IncorrectInputException("Period"
                                      + "", lexer);
                                }
                              }
                            } else {
                              throw new IncorrectInputException("Widget",
                                  lexer);
                            }
                          }
                        } else {
                          throw new IncorrectInputException("Layout", lexer);
                        }
                      } else {
                        throw new IncorrectInputException("Right parenthesis",
                            lexer);
                      }
                    } else {
                      throw new IncorrectInputException("Number", lexer);
                    }
                  } else {
                    throw new IncorrectInputException("Comma", lexer);
                  }
                } else {
                  throw new IncorrectInputException("Number", lexer);
                }
              } else {
                throw new IncorrectInputException("Left parenthesis", lexer);
              }
            } else {
              throw new IncorrectInputException("Quotation mark", lexer);
            }
          } else {
            throw new IncorrectInputException("String", lexer);
          }
        } else {
          throw new IncorrectInputException("Quotation mark", lexer);
        }
      } else {
        throw new IncorrectInputException("Window", lexer);
      }
      return true;
    }

    private boolean widgets() throws IncorrectInputException {
      if (token == Lexer.Token.END) {
        token = lexer.getNextToken();
        return true;
      } else if (widget()) {
        widgets();
        return true;
      }
      return false;
    }

    private boolean widget() throws IncorrectInputException {
      switch (token) {
        case BUTTON:
          return newButton();
        case GROUP:
          return newGroup();
        case LABEL:
          return newLabel();
        case PANEL:
          return newPanel();
        case TEXTFIELD:
          return newTextField();
        default:
          throw new IncorrectInputException("Widget", lexer);
      }
    }

    private boolean newButton() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.QUOTATION_MARK) {
        token = lexer.getNextToken();
        if (token == Lexer.Token.STRING) {
          gui.addButton(lexer.getCurrentLexeme());
          token = lexer.getNextToken();
          if (token == Lexer.Token.QUOTATION_MARK) {
            token = lexer.getNextToken();
            if (token == Lexer.Token.SEMICOLON) {
              token = lexer.getNextToken();
              return true;
            } else {
              throw new IncorrectInputException("Semicolon", lexer);
            }
          } else {
            throw new IncorrectInputException("Quotation mark", lexer);
          }
        } else {
          throw new IncorrectInputException("String", lexer);
        }
      } else {
        throw new IncorrectInputException("Quotation mark", lexer);
      }
    }

    private boolean newGroup() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.RADIO) {
        gui.addButtonGroup();
        if (radioButtons()) {
          if (token == Lexer.Token.SEMICOLON) {
            token = lexer.getNextToken();
            return true;
          } else {
            throw new IncorrectInputException("Semicolon", lexer);
          }
        }
      } else {
        throw new IncorrectInputException("Radio buttons", lexer);
      }
      return false;
    }

    private boolean newLabel() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.QUOTATION_MARK) {
        token = lexer.getNextToken();
        if (token == Lexer.Token.STRING) {
          gui.addLabel(lexer.getCurrentLexeme());
          token = lexer.getNextToken();
          if (token == Lexer.Token.QUOTATION_MARK) {
            token = lexer.getNextToken();
            if (token == Lexer.Token.SEMICOLON) {
              token = lexer.getNextToken();
              return true;
            } else {
              throw new IncorrectInputException("Semicolon", lexer);
            }
          } else {
            throw new IncorrectInputException("Quotation mark", lexer);
          }
        } else {
          throw new IncorrectInputException("String", lexer);
        }
      } else {
        throw new IncorrectInputException("Quotation mark", lexer);
      }
    }

    private boolean newPanel() throws IncorrectInputException {
      gui.addPanel();
      token = lexer.getNextToken();
      if (token == Lexer.Token.LAYOUT) {
        if (layout()) {
          if (widgets()) {
            if (token == Lexer.Token.SEMICOLON) {
              gui.setCurrentContainerBack();
              token = lexer.getNextToken();
              return true;
            } else {
              throw new IncorrectInputException("Semicolon", lexer);
            }
          }
        }
      } else {
        throw new IncorrectInputException("Layout", lexer);
      }
      return false;
    }

    private boolean newTextField() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.NUMBER) {
        gui.addTextField(Integer.parseInt(lexer.getCurrentLexeme()));
        token = lexer.getNextToken();
        if (token == Lexer.Token.SEMICOLON) {
          token = lexer.getNextToken();
          return true;
        } else {
          throw new IncorrectInputException("Semicolon", lexer);
        }
      } else {
        throw new IncorrectInputException("Number", lexer);
      }
    }

    private boolean radioButtons() throws IncorrectInputException {
      if (token == Lexer.Token.RADIO) {
        token = lexer.getNextToken();
        if (radioButton()) {
          radioButtons();
          return true;
        }
        return false;
      } else if (token == Lexer.Token.END) {
        token = lexer.getNextToken();
        return true;
      } else {
        throw new IncorrectInputException("Radio button or end", lexer);
      }
    }

    private boolean radioButton() throws IncorrectInputException {
      if (token == Lexer.Token.QUOTATION_MARK) {
        token = lexer.getNextToken();
        if (token == Lexer.Token.STRING) {
          gui.addRadioButton(lexer.getCurrentLexeme());
          token = lexer.getNextToken();
          if (token == Lexer.Token.QUOTATION_MARK) {
            token = lexer.getNextToken();
            if (token == Lexer.Token.SEMICOLON) {
              token = lexer.getNextToken();
              return true;
            } else {
              throw new IncorrectInputException("Semicolon", lexer);
            }
          } else {
            throw new IncorrectInputException("Quotation mark", lexer);
          }
        } else {
          throw new IncorrectInputException("String", lexer);
        }
      } else {
        throw new IncorrectInputException("Quotation mark", lexer);
      }
    }

    private boolean layout() throws IncorrectInputException {
      return layoutType();
    }

    private boolean layoutType() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.FLOW) {
        return setFlowLayout();
      } else if (token == Lexer.Token.GRID) {
        return setGridLayout();
      } else {
        throw new IncorrectInputException("Grid or flow", lexer);
      }
    }

    private boolean setFlowLayout() throws  IncorrectInputException {
      gui.setFlowLayout();
      token = lexer.getNextToken();
      if (token == Lexer.Token.COLON) {
        token = lexer.getNextToken();
        return true;
      } else {
        throw new IncorrectInputException("Colon", lexer);
      }
    }

    private boolean setGridLayout() throws IncorrectInputException {
      token = lexer.getNextToken();
      if (token == Lexer.Token.LEFT_PARENTHESIS) {
        token = lexer.getNextToken();
        if (token == Lexer.Token.NUMBER) {
          String first = lexer.getCurrentLexeme();
          token = lexer.getNextToken();
          if (token == Lexer.Token.COMMA) {
            token = lexer.getNextToken();
            if (token == Lexer.Token.NUMBER) {
              String second = lexer.getCurrentLexeme();
              token = lexer.getNextToken();
              if (token == Lexer.Token.RIGHT_PARENTHESIS) {
                gui.setGridLayout(Integer.parseInt(first),
                    Integer.parseInt(second));
                token = lexer.getNextToken();
                if (token == Lexer.Token.COLON) {
                  token = lexer.getNextToken();
                  return true;
                } else {
                  throw new IncorrectInputException("Colon", lexer);
                }
              } else if (token == Lexer.Token.COMMA) {
                token = lexer.getNextToken();
                if (token == Lexer.Token.NUMBER) {
                  String third = lexer.getCurrentLexeme();
                  token = lexer.getNextToken();
                  if (token == Lexer.Token.COMMA) {
                    token = lexer.getNextToken();
                    if (token == Lexer.Token.NUMBER) {
                      gui.setGridLayout(Integer.parseInt(first),
                          Integer.parseInt(second),
                          Integer.parseInt(third),
                          Integer.parseInt(lexer.getCurrentLexeme()));
                      token = lexer.getNextToken();
                      if (token == Lexer.Token.RIGHT_PARENTHESIS) {
                        token = lexer.getNextToken();
                        if (token == Lexer.Token.COLON) {
                          token = lexer.getNextToken();
                          return true;
                        } else {
                          throw new IncorrectInputException("Colon", lexer);
                        }
                      } else {
                        throw new IncorrectInputException("Right parenthesis",
                            lexer);
                      }
                    } else {
                      throw new IncorrectInputException("Number", lexer);
                    }
                  } else {
                    throw new IncorrectInputException("Comma", lexer);
                  }
                } else {
                  throw new IncorrectInputException("Number", lexer);
                }
              } else {
                throw new IncorrectInputException("Comma or right "
                    + "parenthesis", lexer);
              }
            } else {
              throw new IncorrectInputException("Number", lexer);
            }
          } else {
            throw new IncorrectInputException("Comma", lexer);
          }
        } else {
          throw new IncorrectInputException("Number", lexer);
        }
      } else {
        throw new IncorrectInputException("Left parenthesis", lexer);
      }
    }
  }
