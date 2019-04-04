import javax.swing.*;

public class Main {
 public static void main(String[] args) {
  Menu menu = new Menu(); // Apre il menu di gioco
  menu.setSize(572,388); // Regola le dimensioni della finestra
  menu.setResizable(false); // La finestra non puo' essere ridimensionata
  menu.setVisible(true); // La finestra e' visibile
  menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  } } // Chiude automaticamente il programma se si chiude la finestra