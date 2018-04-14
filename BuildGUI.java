/*
* File: BuildGUI.java
* Author: Claire Stovall
* Date: April 8, 2018
* Purpose: This program is the main class for the program that parses,
* using recursive descent, a GUI definition language defined in an input file
* and generates the GUI that it defines. The class provides the methods
* that build the GUI.
*/

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BuildGUI {
  private JFrame window;
  private Container currentContainer;
  private ButtonGroup currentButtonGroup;
  private Stack<Container> containerList;

  // Constructor
  public BuildGUI() {
    window = new JFrame();
    window.setLocationRelativeTo(null);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    currentContainer = window;
    containerList = new Stack<Container>();
  }

  // Display the GUI
  public void display() {
    window.setVisible(true);
  }

  // Set window options
  public void setTitle(String title) {
    window.setTitle(title);
  }

  // Set setDimensions
  public void setDimensions(int width, int height) {
    window.setSize(width, height);
  }

  // Set flow layout
  public void setFlowLayout() {
    currentContainer.setLayout(new FlowLayout());
  }

  // Set grid layout
  public void setGridLayout(int first, int second, int third, int fourth) {
    currentContainer.setLayout(new GridLayout(first, second, third, fourth));
  }

  // Set grid layout
  public void setGridLayout(int first, int second) {
    currentContainer.setLayout(new GridLayout(first, second));
  }

  // Add button
  public void addButton(String name) {
    currentContainer.add(new JButton(name));
  }

  // Add label
  public void addLabel(String text) {
    currentContainer.add(new JLabel(text));
  }

  // Add text field
  public void addTextField(int width) {
    currentContainer.add(new JTextField(width));
  }

  // Add button group
  public void addButtonGroup() {
    ButtonGroup buttonGroup = new ButtonGroup();
    currentButtonGroup = buttonGroup;
  }

  // Add radio button
  public void addRadioButton(String label) {
    JRadioButton button = new JRadioButton(label);
    currentContainer.add(button);
    currentButtonGroup.add(button);
  }

  // Add panel
  public void addPanel() {
    JPanel panel = new JPanel();
    currentContainer.add(panel);
    containerList.push(currentContainer);
    currentContainer = panel;
  }

  // Set current container to prior current container
  public void setCurrentContainerBack() {
    currentContainer = containerList.pop();
  }
}
