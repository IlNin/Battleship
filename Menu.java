import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class Menu extends JFrame {
 JPanel p; // Pannello principale a cui verranno aggiunti tutti gli altri componenti
 JLabel logo; // Label con l'immagine contenente il logo
 JPanel pulsanti; // Pannello che conterra' i pulsanti del menu
 
 public Menu() {
  setFocusable(true);
  requestFocus();
  
  this.setTitle("BATTAGLIA NAVALE - Menu Principale"); // Titolo che appare in alto alla finestra
  p = new JPanel();
  p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS)); // Il pannello aggiungera' i componenti a colonna
  p.setVisible(true);
  this.setContentPane(p); // p diventa il pannello principale
  
  logo = new JLabel(new ImageIcon("Menu.PNG")); // Carica l'immagine con il logo...
  logo.setAlignmentX(Component.CENTER_ALIGNMENT); // ... e la piazza al centro della riga
  p.add(logo);
  
  pulsanti = new JPanel();
  pulsanti.setLayout(new BoxLayout(pulsanti, BoxLayout.LINE_AXIS)); // Il pannello aggiungera' i componenti in sequenza su una riga
  
  p.add(pulsanti);
  
  JButton gioca = new JButton("Gioca");
  JButton about = new JButton("About");
  JButton esci = new JButton("Esci");
  pulsanti.add(gioca);
  pulsanti.add(about);
  pulsanti.add(esci);
  
  gioca.addActionListener(new ActionListener() { // Il giocatore clicca il pulsante "Gioca"
   public void actionPerformed(ActionEvent e) {
    Gioco gioco = new Gioco();
    gioco.setSize(240, 625);
    gioco.setResizable(false);
    gioco.setVisible(true);
    gioco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    setVisible(false);
    dispose(); } });
	
  about.addActionListener(new ActionListener() { // Il giocatore clicca il pulsante "About"
   public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(null, "- BENVENUTO A BATTAGLIA NAVALE! -\nClicca le caselle sulla griglia avversaria\nnel tentativo di affondare le sue navi prima che\npossa fare lo stesso contro di te!\n\n>>> Creato da Luca Sannino <<<", "Battaglia Navale - About", JOptionPane.PLAIN_MESSAGE); } });
  
  esci.addActionListener(new ActionListener() { // Il giocatore clicca il pulsante "Esci"
   public void actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose(); } }); } }
  
 