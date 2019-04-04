import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class Gioco extends JFrame {
 JPanel p; // Pannello principale a cui verranno aggiunti tutti gli altri componenti
 JPanel pGiocatore; // Pannello contenente la griglia del giocatore
 JLabel scrittaGiocatore; // JLabel che indica che la griglia e' del giocatore
 JLabel[][] grigliaGiocatore; // Griglia del giocatore
 int[][] indiceGiocatore; // Griglia del giocatore, ma in formato int
 JPanel pComputer; // Pannello contenente la griglia della CPU
 JLabel scrittaComputer; // JLabel che indica che la griglia e' della CPU
 JLabel[][] grigliaComputer; // Griglia della CPU
 JLabel nomeFeed; // JLabel con la scritta "Feed di gioco" (Il feed descrive cosa e' successo nell'ultimo turno)
 JLabel feed; // Prima riga del feed
 JLabel feed2; // Seconda riga del feed
 JLabel feed3; // Terza riga del feed
 JButton tiraCPU; // Pulsante che permette di tirare alla CPU quando il turno del giocatore e' terminato
 
 int colpiGiocatore = 0; // Colpi a segno da parte del giocatore
 int colpiComputer = 0; // Colpi a segno da parte della CPU
 int[] naviGrandiColpite = {0, 0, 0, 0, 0, 0, 0}; // Ogni int dell'array tiene conto delle caselle di una specifica nave colpite dal giocatore (NOTA: il primo elemento e' inutilizzato)
 int[] naviGrandiColpiteCPU = {0, 0, 0}; // Ogni int dell'array tiene il numero di navi affondati dalla CPU per tipologia (NOTA: leggermente differente rispetto a naviGrandiColpite, che tiene anche conto delle caselle)
 int colpiNaveAttuale = 0; // Indica il numero di volte che la CPU ha colpito la nave attuale
 int ultimoX = 0; // Coordinata X dell'ultimo tiro andato a segno della CPU
 int ultimoY = 0; // Coordinata Y dell'ultimo tiro andato a segno della CPU
 boolean orizzontale = false; // Indica se la CPU pensa che la nave attualmente colpita sia disposta orizzontalmente
 boolean verticale = false; // Indica se la CPU pensa che la nave attualmente colpita sia disposta verticalmente
 boolean turnoGiocatore = true; // Indica se tocca al giocatore
 
 JLabel[][] grigliaComputerVera; // Griglia della CPU con la posizione di tutte le navi (che ovviamente non viene visualizzata nella UI)
 int[][] indiceComputer; // Griglia del computer, ma in formato int
 
 public Gioco() {
  this.setTitle("In gioco"); // Titolo che appare in alto alla finestra
  
  setFocusable(true);
  requestFocus();
  
  p = new JPanel();
  p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS)); // Il pannello aggiungera' i componenti a colonna
  p.setVisible(true);
  this.setContentPane(p); // p diventa il pannello principale
  
  scrittaGiocatore = new JLabel("Griglia del Giocatore: ");
  scrittaGiocatore.setAlignmentX(Component.CENTER_ALIGNMENT);
  p.add(scrittaGiocatore);
  
  pGiocatore = new JPanel(); // Crea il pannello che conterra' la griglia del giocatore
  pGiocatore.setBackground(Color.BLACK); // Lo sfondo e' nero per distinguerlo dalle caselle bianche
  pGiocatore.setVisible(true);
  p.add(pGiocatore);
  
  scrittaComputer = new JLabel("Griglia del Computer: ");
  scrittaComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
  p.add(scrittaComputer);
  
  pComputer = new JPanel(); // Crea il pannello che conterra' la griglia della CPU
  pComputer.setBackground(Color.BLACK);
  pComputer.setVisible(true);
  p.add(pComputer);
  
  nomeFeed = new JLabel("Feed di gioco: ");
  nomeFeed.setAlignmentX(Component.CENTER_ALIGNMENT);
  p.add(nomeFeed);
  
  feed = new JLabel("Premi una casella della griglia nemica!"); // Il feed viene inizializzato con un messaggio iniziale di una riga
  feed.setBackground(Color.BLACK); // Il feed avra' testo bianco su sfondo nero
  feed.setForeground(Color.WHITE);
  feed.setOpaque(true);
  feed.setAlignmentX(Component.CENTER_ALIGNMENT);
  feed2 = new JLabel(" ");
  feed2.setBackground(Color.BLACK);
  feed2.setForeground(Color.WHITE);
  feed2.setOpaque(false);
  feed2.setAlignmentX(Component.CENTER_ALIGNMENT);
  feed3 = new JLabel(" ");
  feed3.setBackground(Color.BLACK);
  feed3.setForeground(Color.WHITE);
  feed3.setOpaque(false);
  feed3.setAlignmentX(Component.CENTER_ALIGNMENT);
  p.add(feed);
  p.add(feed2);
  p.add(feed3);
  
  tiraCPU = new JButton("> Turno CPU <");
  tiraCPU.setAlignmentX(Component.CENTER_ALIGNMENT);
  tiraCPU.setEnabled(false); // Il giocatore inizia sempre per primo, quindi il pulsante e' inizialmente disabilitato
  p.add(tiraCPU);
  
  grigliaGiocatore = new JLabel[11][11]; // Crea le griglie, gli indici e inserisce al loro interno le navi in maniera casuale
  indiceGiocatore = new int[11][11];
  creaGriglia(grigliaGiocatore, pGiocatore); 
  grigliaComputer = new JLabel[11][11];
  creaGriglia(grigliaComputer, pComputer);
  grigliaComputerVera = new JLabel[11][11];
  indiceComputer = new int[11][11];
  creaGriglia(grigliaComputerVera, null);
  inputGriglia(); // Permette al giocatore di cliccare e interagire con la griglia della CPU
  preparaGrigliaCasuale(grigliaGiocatore, indiceGiocatore);
  preparaGrigliaCasuale(grigliaComputerVera, indiceComputer); 
  
  tiraCPU.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) { // Il giocatore preme il pulsanto "Turno della CPU"
    boolean risultato = IAnemica(); // Calcola il tiro della CPU
	if (risultato == true) { // Colpito! La CPU puo' tirare di nuovo
	 turnoGiocatore = false;
     if (colpiComputer == 20) { // Controlla se la CPU ha vinto
      gameOver(false);	} }
	else { // Acqua! Il turno passa al giocatore
	 turnoGiocatore = true;
     tiraCPU.setEnabled(false); } } }); } 
	
 public void creaGriglia(JLabel[][] griglia, JPanel panel) { // Crea le griglie di gioco
  for (int i = 0; i < 11; i++) {
   for (int j = 0; j < 11; j++) {
    if (i == 0 && j == 0) { // La prima casella, quella in alto e a destra, appare completamente nera per mimetizzarsi con lo sfondo nero
	 griglia[i][j] = new JLabel("     ");
	 griglia[i][j].setBackground(Color.BLACK);
	 griglia[i][j].setOpaque(true);
	 if (panel != null) {
	  panel.add(griglia[i][j]); } }
	 
	else if (i != 0 && j == 0) { // Le caselle a destra di ogni riga contengono le coordinate in numeri e sono tutte bianche
	 if (i == 10) { // Il numero 10 viene rappresentato con una 'x' perche' due cifre disallineano le righe
	  griglia[i][j] = new JLabel("  " + "x" + " "); }
	 else {
	  griglia[i][j] = new JLabel("  " + Integer.toString(i) + " "); }
	 griglia[i][j].setBackground(Color.WHITE);
	 griglia[i][j].setOpaque(true);
	 if (panel != null) {
	  panel.add(griglia[i][j]); } }
	 
	else if (i == 0 && j != 0) { // Le caselle in cima ad ogni colonna contengono le coordinate in numeri e sono tutte bianche
	 if (j == 10) {
	  griglia[i][j] = new JLabel("  " + "x" + " "); }
	 else {
	  griglia[i][j] = new JLabel("  " + Integer.toString(j) + " "); }
	 griglia[i][j].setBackground(Color.WHITE);
	 griglia[i][j].setOpaque(true);
	 if (panel != null) {
	  panel.add(griglia[i][j]); } }
	 
	else { // Tutte le altre caselle sono colorate di blu
	 griglia[i][j] = new JLabel("     ");
	 griglia[i][j].setBackground(Color.BLUE); 
	 griglia[i][j].setOpaque(true);
	 if (panel != null) {
	  panel.add(griglia[i][j]); } } } } }
 
 public void inputGriglia() { // Permette al giocatore di cliccare e interagire con la griglia della CPU
  for (int i = 0; i < 11; i++) {
   for (int j = 0; j < 11; j++) { 
    final int I = i;
    final int J = j;
    grigliaComputer[i][j].addMouseListener(new MouseListener() {
     public void mousePressed(MouseEvent e) {}
     public void mouseReleased(MouseEvent e) {}
     public void mouseExited(MouseEvent e) {}
     public void mouseEntered(MouseEvent e) {}
     public void mouseClicked(MouseEvent e) { // Il giocatore clicca una casella sulla griglia avversaria
	  if (grigliaComputer[I][J].getBackground() == Color.BLUE && turnoGiocatore == true) { // Controlla se il tiro e' valido
	   boolean esito = mossaUmanaRealizzazione(I, J); // Calcola se il tiro e' andato a segno
       if (esito == false) { // Acqua! Tocca alla CPU
        turnoGiocatore = false;
        tiraCPU.setEnabled(true); } // Ora il pulsante "Turno della CPU" diventa disponibile
       else { // Colpito! Tocca nuovamente al giocatore
        colpiGiocatore = colpiGiocatore + 1;
        if (colpiGiocatore == 20) { // Controlla se il giocatore ha vinto la partita
         gameOver(true);		} } } } } ); } } }
    
 public boolean controllaSpazio(JLabel[][] griglia, int riga, int colonna) { // Guarda se c'e' lo spazio per inserire una nave
  if (riga == 0 || colonna == 0) { // Queste posizione contengono le coordinate della griglia
   return false; }
  boolean risultato = true;
  for (int i = riga-1; i <= riga+1; i++) { // Data una posizione (x, y) controlla che le caselle da (x-1, y-1) a (x+1, y+1) siano libere
	  for (int j = colonna-1; j <= colonna+1; j++) {
	   try {
	    if (griglia[i][j].getBackground() != Color.BLUE && i != 0 && j != 0) {
	     risultato = false; } }
       catch(ArrayIndexOutOfBoundsException e) { } } }
  return risultato; }
 
 public void preparaGrigliaCasuale(JLabel[][] griglia, int[][] indice) { // Inserisce casualmente le navi in una griglia
  // Prepara le navi da una casella
  for (int contatore = 0; contatore < 4; contatore++) { // Ci sono 4 navi da una casella
   boolean posizioneValida = false;
   while (posizioneValida == false) {
    Random randX = new Random();
    Random randY = new Random();
    int X = randX.nextInt(10) + 1;
    int Y = randY.nextInt(10) + 1;
    if (griglia[X][Y].getBackground() == Color.BLUE) {
	 if (controllaSpazio(griglia, X, Y) == true) { // Se la posizione e' buona, controlla che lo siano anche le caselle circostanti
	   griglia[X][Y].setBackground(Color.RED); // Le navi da una casella sono ROSSE
	   indice[X][Y] = 7; // Sulla griglia di int, le navi rosse sono rappresentate da un sette
       posizioneValida = true; } } } }
	   
  // Prepara le navi da due caselle
  for (int contatore = 1; contatore < 4; contatore++) { // Ci sono 3 navi da due caselle
   boolean posizioneValida = false;
   while (posizioneValida == false) {
    try {
     Random randX = new Random();
     Random randY = new Random();
     int X = randX.nextInt(10) + 1;
     int Y = randY.nextInt(10) + 1;
	 if (griglia[X][Y].getBackground() == Color.BLUE) {
	  if (controllaSpazio(griglia, X, Y) == true) {
	   Random randPosizione = new Random();
	   int orizzontaleOverticale = randPosizione.nextInt(2); // Sceglie se piazzare la nave orizzontalmente o verticalmente (0 == orizzontale, 1 == verticale)
	   if (orizzontaleOverticale == 0) { // Decide di piazzare la nave orizzontalmente
	    if (controllaSpazio(griglia, X, Y+1) == true) {
	     griglia[X][Y+1].setBackground(Color.ORANGE); // Le navi da due caselle sono ARANCIONI
		 griglia[X][Y].setBackground(Color.ORANGE);
		 indice[X][Y+1] = contatore; // La nave e' contrassegnata da un numero univoco
		 indice[X][Y] = contatore;
		 posizioneValida = true; } }
	   else { // Decide di piazzare la nave verticalmente
	     if (controllaSpazio(griglia, X+1, Y) == true) {
	      griglia[X+1][Y].setBackground(Color.ORANGE);
		  griglia[X][Y].setBackground(Color.ORANGE);
		  indice[X+1][Y] = contatore;
		  indice[X][Y] = contatore;
		  posizioneValida = true; } } } } }
	 catch(IndexOutOfBoundsException e) {} } }
	 
  // Prepara le navi da tre caselle
  for (int contatore = 4; contatore < 6; contatore++) { // Ci sono 2 navi da tre caselle
   boolean posizioneValida = false;
   while (posizioneValida == false) {
    try {
	 Random randX = new Random();
	 Random randY = new Random();
	 int X = randX.nextInt(10) + 1;
	 int Y = randY.nextInt(10) + 1;
	 if (griglia[X][Y].getBackground() == Color.BLUE) {
	  if (controllaSpazio(griglia, X, Y) == true) {
	   Random randPosizione = new Random();
	   int orizzontaleOverticale = randPosizione.nextInt(2); // (0 == orizzontale, 1 == verticale)
	   if (orizzontaleOverticale == 0) {
		 if (controllaSpazio(griglia, X, Y+1) && controllaSpazio(griglia, X, Y-1)) {
          griglia[X][Y+1].setBackground(Color.YELLOW); // Le navi da tre caselle sono GIALLE
          griglia[X][Y-1].setBackground(Color.YELLOW);
          griglia[X][Y].setBackground(Color.YELLOW);
		  indice[X][Y+1] = contatore;
		  indice[X][Y-1] = contatore;
		  indice[X][Y] = contatore;
          posizioneValida = true; } }
       else {
	     if (controllaSpazio(griglia, X+1, Y) == true && controllaSpazio(griglia, X-1, Y)) {
	      griglia[X+1][Y].setBackground(Color.YELLOW);
		  griglia[X-1][Y].setBackground(Color.YELLOW);
		  griglia[X][Y].setBackground(Color.YELLOW);
		  indice[X+1][Y] = contatore;
		  indice[X-1][Y] = contatore;
		  indice[X][Y] = contatore;
		  posizioneValida = true; } } } } }
	catch(IndexOutOfBoundsException e) {} } }
	 
  // Prepara la nave da quattro caselle
  boolean posizioneValida = false;
   while (posizioneValida == false) {
    try {
	 Random randX = new Random();
	 Random randY = new Random();
	 int X = randX.nextInt(10) + 1;
	 int Y = randY.nextInt(10) + 1;
	 if (griglia[X][Y].getBackground() == Color.BLUE) {
	  if (controllaSpazio(griglia, X, Y) == true) {
	   Random randPosizione = new Random();
	   int orizzontaleOverticale = randPosizione.nextInt(2); // (0 == orizzontale, 1 == verticale)
	   if (orizzontaleOverticale == 0) {
		 if (controllaSpazio(griglia, X, Y+1) && controllaSpazio(griglia, X, Y-1) && controllaSpazio(griglia, X, Y-2)) {
          griglia[X][Y+1].setBackground(Color.GREEN); // Le navi da quattro caselle sono VERDI
          griglia[X][Y-1].setBackground(Color.GREEN);
		  griglia[X][Y-2].setBackground(Color.GREEN);
          griglia[X][Y].setBackground(Color.GREEN);
		  indice[X][Y+1] = 6;
		  indice[X][Y-1] = 6;
		  indice[X][Y-2] = 6;
		  indice[X][Y] = 6;
          posizioneValida = true; } }
	   else {
	     if (controllaSpazio(griglia, X+1, Y) == true && controllaSpazio(griglia, X-1, Y) && controllaSpazio(griglia, X-2, Y)) {
	      griglia[X+1][Y].setBackground(Color.GREEN);
		  griglia[X-1][Y].setBackground(Color.GREEN);
		  griglia[X-2][Y].setBackground(Color.GREEN);
		  griglia[X][Y].setBackground(Color.GREEN);
		  indice[X+1][Y] = 6;
		  indice[X-1][Y] = 6;
		  indice[X-2][Y] = 6;
		  indice[X][Y] = 6;
		  posizioneValida = true; } } } } }
	catch(IndexOutOfBoundsException e) {} } }
	 
 public void liberaSpazio(JLabel[][] griglia, int riga, int colonna) { // Libera lo spazio attorno ad una nave una volta affondata
  griglia[riga][colonna].setBackground(Color.MAGENTA); // La casella attuale diventa momentaneamente magenta per evitare loop infiniti nella ricorsione
  for (int i = riga-1; i<=riga+1; i++) {
   for (int j = colonna-1; j<=colonna+1; j++) {
    try {
	 if (griglia[i][j].getBackground() == Color.BLACK) { // Per ripulire la zona circonstante ad una nave, l'algoritmo si muove tra una casella nera e l'altra
	  liberaSpazio(griglia, i, j); }
	 else if (griglia[i][j].getBackground() != Color.BLACK && i != 0 && j != 0) {
	  griglia[i][j].setBackground(Color.WHITE); } }
	catch(IndexOutOfBoundsException e) {} } }
  griglia[riga][colonna].setBackground(Color.BLACK); } // La casella attuale diventa nuovamente nera
 
 public boolean ipotesiTiro(int x, int y, int n) { // La CPU controlla se potrebbe colpire la nave da n elementi se tira al punto (x, y)
  // TEST 0: controlla se la casella (x, y) e' accettabile
  try {
   if (grigliaGiocatore[x][y].getBackground() == Color.WHITE || grigliaGiocatore[x][y].getBackground() == Color.BLACK) {
    return false; } }
  catch(IndexOutOfBoundsException e) {
   return false; }
  if (n == 1) { // Se n e' 1, allora il test per gli (n-1) punti e' gia' passato
   return true; }
   
  // TEST 1: controlla gli (n-1) punti in basso a quello casualmente scelto
  boolean esito = true; 
  for (int i = x; i < (x+n); i++) {
   try {
    if (grigliaGiocatore[i][y].getBackground() == Color.WHITE || grigliaGiocatore[i][y].getBackground() == Color.BLACK) {
	 esito = false; } }
   catch(IndexOutOfBoundsException e) {
    esito = false; } }
  if (esito == true) {
   return esito; }
   
  // TEST 2: controlla gli (n-1) punti a destra di quello casualmente scelto
  esito = true; 
  for (int i = y; i < (y+n); i++) {
   try {
    if (grigliaGiocatore[x][i].getBackground() == Color.WHITE || grigliaGiocatore[x][i].getBackground() == Color.BLACK) {
	 esito = false; } }
   catch(IndexOutOfBoundsException e) {
    esito = false; } }
  if (esito == true) {
   return esito; }
  else {
   return false; } }
	 
 public void evitaLoopInfiniti() { // Evita loop infiniti nel caso l'unica mossa disponibile rimasta alla CPU sia quella di scegliere una casella non valida
  if (colpiNaveAttuale > 1) {
   if (((ultimoX+1) == 11 || grigliaGiocatore[ultimoX+1][ultimoY].getBackground() == Color.WHITE) && verticale == true) {
    ultimoX = ultimoX - (colpiNaveAttuale-1); }
   else if (((ultimoX-1) == 0 || grigliaGiocatore[ultimoX-1][ultimoY].getBackground() == Color.WHITE) && verticale == true) {
    ultimoX = ultimoX + (colpiNaveAttuale-1); }
   else if (((ultimoY+1) == 11 || grigliaGiocatore[ultimoX][ultimoY+1].getBackground() == Color.WHITE) && orizzontale == true) {
    ultimoY = ultimoY - (colpiNaveAttuale-1); }
   else if (((ultimoY-1) == 0 || grigliaGiocatore[ultimoX][ultimoY-1].getBackground() == Color.WHITE) && orizzontale == true) {
    ultimoY = ultimoY + (colpiNaveAttuale-1); } } }
   
 public boolean IAnemica() { // Turno della CPU
  boolean esito = false; // Indica se la CPU ha colpito qualcosa
  int X = 0;
  int Y = 0;
  
  // STRATEGIA 1: Nessuna nave e' stata attualmente colpita senza che non sia affondata: la CPU tira casualmente
  if (colpiNaveAttuale == 0) {
   boolean colpoValido = false;
   while (colpoValido != true) {
    Random randX = new Random();
    Random randY = new Random();
    X = randX.nextInt(10) + 1;
    Y = randY.nextInt(10) + 1;
	
	// STRATEGIA 1.1: Pur tirando casualmente, la CPU cerca sempre di colpire la nave piu' grande possibile
	if (naviGrandiColpiteCPU[2] < 1) { // La CPU cerca di colpire per prima la nave da 4 elementi
	 if (ipotesiTiro(X, Y, 4) != true) {
	  continue; }
	 colpoValido = true; }
	else if (naviGrandiColpiteCPU[1] < 2) { // Poi quelle da 3
	 if (ipotesiTiro(X, Y, 3) != true) {
	  continue; }
	 colpoValido = true; }
	else if (naviGrandiColpiteCPU[0] < 3) { // Poi quelle da 2
	 if (ipotesiTiro(X, Y, 2) != true) {
	  continue; }
	 colpoValido = true; } 
	else if (ipotesiTiro(X, Y, 1) == true)  { // Infine quelle da 1
	 colpoValido = true; } }
	 
   feed.setText("Il computer ha tirato nel punto [" + X + ", " + Y + "]");
   if (grigliaGiocatore[X][Y].getBackground() != Color.BLUE) { // La CPU colpisce una nave!
	colpiComputer = colpiComputer + 1;
	esito = true;
	feed2.setText("Il computer ha fatto centro!");
	feed2.setOpaque(true);
    if (grigliaGiocatore[X][Y].getBackground() == Color.RED) { // Se la nave e' da 1, il computer la affonda immediatamente
	 grigliaGiocatore[X][Y].setBackground(Color.BLACK);
	 liberaSpazio(grigliaGiocatore, X, Y);
     feed3.setText("Il computer ha affondato una nave da 1!");
     feed3.setOpaque(true); }
	else { // Se la nave NON è da 1, il computer la colpisce e al turno successivo tentera' di affondarla
	 grigliaGiocatore[X][Y].setBackground(Color.BLACK);
	 colpiNaveAttuale = colpiNaveAttuale + 1;
	 ultimoX = X; 
	 ultimoY = Y;
	 feed3.setText(" ");
     feed3.setOpaque(false); } }
   else { // La CPU computer non colpisce nulla
	grigliaGiocatore[X][Y].setBackground(Color.WHITE);
    feed2.setText("Il computer ha mancato il bersaglio!");
    feed2.setOpaque(true);
	feed3.setText(" ");
    feed3.setOpaque(false); } }
	
  // STRATEGIA 2: La CPU ha appena colpito una nave senza affondarla: il suo tiro ora e' piu' mirato
  else { 
   boolean colpoValido = false;
   evitaLoopInfiniti(); // Aggiusta ultimoX e ultimoY per evitare loop infiniti dovuti a situazioni particolari
   while (colpoValido != true) {
	Random randAggiungi = new Random();
	if (orizzontale == false && verticale == false) { // Il computer ancora non sa come e' disposta la nave
	 Random randScelta = new Random();
	 int scelta = randScelta.nextInt(2);
	 if (scelta == 0) { // Prova a vedere se la nave e' disposta orizzontalmente
	  X = ultimoX;
	  Y = randAggiungi.nextInt(3) + (ultimoY-1); }
	 else { // Prova a vedere se la nave e' disposta verticalmente
	  X = randAggiungi.nextInt(3) + (ultimoX-1);
	  Y = ultimoY; } }
	else if (orizzontale == true && verticale == false) { // Il computer sa che la nave e' disposta orizzontalmente
	 X = ultimoX;
	 Y = randAggiungi.nextInt(3) + (ultimoY-1); }
	else if (orizzontale == false && verticale == true) { // Il computer sa che la nave e' disposta verticalmente
	 X = randAggiungi.nextInt(3) + (ultimoX-1);
	 Y = ultimoY; }
	if (ipotesiTiro(X, Y, 1) == true) {
	 colpoValido = true; } }
   feed.setText("Il computer ha tirato nel punto [" + X + ", " + Y + "]"); 
   if (grigliaGiocatore[X][Y].getBackground() != Color.BLUE) { // La CPU colpisce una nave
	colpiComputer = colpiComputer + 1;
	esito = true;
	feed2.setText("Il computer ha fatto centro!");
	feed2.setOpaque(true);
	  
	// STRATEGIA 2.1: Se la CPU colpisce una nave da 2 caselle, questa sara' affondata sicuramente e nel prossimo turno tirera' casualmente
	if (grigliaGiocatore[X][Y].getBackground() == Color.ORANGE) {
	 grigliaGiocatore[X][Y].setBackground(Color.BLACK);
	 naviGrandiColpiteCPU[0] = naviGrandiColpiteCPU[0]+1;
	 colpiNaveAttuale = 0;
	 ultimoX = 0;
	 ultimoY = 0;
	 orizzontale = false;
	 verticale = false;
	 liberaSpazio(grigliaGiocatore, X, Y);
	 feed3.setText("Il computer ha affondato una nave da 2!");
     feed3.setOpaque(true); }
	   
	// STRATEGIA 2.2: Se la CPU colpisce una nave da 3 casella, potrebbe averla affondata o deve colpirla ancora una volta
	else if (grigliaGiocatore[X][Y].getBackground() == Color.YELLOW) { 
	 grigliaGiocatore[X][Y].setBackground(Color.BLACK);
	 colpiNaveAttuale = colpiNaveAttuale+1;
	 if (colpiNaveAttuale == 3) { // Tutte le tre parti sono state colpite
	  naviGrandiColpiteCPU[1] = naviGrandiColpiteCPU[1]+1;
	  colpiNaveAttuale = 0;
	  ultimoX = 0;
	  ultimoY = 0;
	  orizzontale = false;
	  verticale = false;
	  liberaSpazio(grigliaGiocatore, X, Y);
	  feed3.setText("Il computer ha affondato una nave da 3!");
	  feed3.setOpaque(true); }
	 else { // La nave è stata colpita solo 2 volte
	  if (Y == ultimoY+1 || Y == ultimoY-1) { // La CPU ha abbastanza informazioni per capire se la nave e' verticale o orizzontale
	   orizzontale = true; }
	  else if (X == ultimoX+1 || X == ultimoX-1) {
	   verticale = true; }
	  ultimoX = X;
	  ultimoY = Y;
      feed3.setText(" ");
	  feed3.setOpaque(false); } }
		
	// STRATEGIA 2.3: Se la CPU colpisce una nave da 4 caselle, potrebbe averla affondata o deve colpirla ancora ancora una o due volte
	else if (grigliaGiocatore[X][Y].getBackground() == Color.GREEN) { 
	 grigliaGiocatore[X][Y].setBackground(Color.BLACK);
	 colpiNaveAttuale = colpiNaveAttuale+1;
	 if (colpiNaveAttuale == 4) { // Tutte le quattro parti sono state colpite
	  naviGrandiColpiteCPU[2] = 1;
	  colpiNaveAttuale = 0;
	  ultimoX = 0;
	  ultimoY = 0;
	  orizzontale = false;
	  verticale = false;
	  liberaSpazio(grigliaGiocatore, X, Y);
	  feed3.setText("Il computer ha affondato la nave da 4!");
      feed3.setOpaque(true); }
	 else { // La nave è stata colpita 2 o 3 volte
	  if (Y == ultimoY+1 || Y == ultimoY-1) {
	   orizzontale = true; }
	  else if (X == ultimoX+1 || X == ultimoX-1) {
	   verticale = true; }
	  ultimoX = X;
	  ultimoY = Y;
      feed3.setText(" ");
	  feed3.setOpaque(false); } } }
		
	  
   else { // Colpo a vuoto
	grigliaGiocatore[X][Y].setBackground(Color.WHITE);
	feed2.setText("Il computer ha mancato il bersaglio!");
	feed2.setOpaque(true);
	feed3.setText(" ");
	feed3.setOpaque(false);
   
    // STRATEGIA 2.4: Se la CPU sa che la nave e' orizzontale, ma ha mancato il bersaglio, significa che deve colpire dall'altra parte della nave
    if (orizzontale == true) { 
	 if (grigliaGiocatore[X][ultimoY-1].getBackground() == Color.BLACK) {
      ultimoY = ultimoY - (colpiNaveAttuale-1); }
     else if (grigliaGiocatore[X][ultimoY+1].getBackground() == Color.BLACK) {
	  ultimoY = ultimoY + (colpiNaveAttuale-1); }  }
	
	// STRATEGIA 2.5: Se la CPU sa che la nave e' verticale, ma ha mancato il bersaglio, significa che deve colpire dall'altra parte della nave
	else if (verticale == true) { 
	 if (grigliaGiocatore[ultimoX-1][Y].getBackground() == Color.BLACK) {
	  ultimoX = ultimoX - (colpiNaveAttuale-1); }
	 else if (grigliaGiocatore[ultimoX+1][Y].getBackground() == Color.BLACK) {
	  ultimoX = ultimoX + (colpiNaveAttuale-1); } } } }
	  
  return esito; }
  
 public boolean mossaUmanaRealizzazione(int riga, int colonna) { // Turno del giocatore
  feed.setText("Il Giocatore ha tirato nel punto [ " + riga + ", " + colonna + "]"); // Il feed indica la casella cliccata dal giocatore
  if (grigliaComputerVera[riga][colonna].getBackground() == Color.RED) { // Viene consultata la vera griglia della CPU per controllare l'esito del tiro
   grigliaComputer[riga][colonna].setBackground(Color.BLACK); // Nave da 1 colpita! (Rosso == nave da 1)
   feed2.setText("Il Giocatore ha fatto centro!");
   feed2.setOpaque(true);
   feed3.setText("Il Giocatore ha affondato una nave da 1!"); 
   feed3.setOpaque(true);
   liberaSpazio(grigliaComputer, riga, colonna);
   return true; }
  else {
   for (int i = 1; i < 7; i++) {
    if (indiceComputer[riga][colonna] == i) { // Viene colpita una nave non rossa
	 grigliaComputer[riga][colonna].setBackground(Color.BLACK);
	 feed2.setText("Il Giocatore ha fatto centro!");
	 feed2.setOpaque(true);
	 feed3.setText(" ");
	 feed3.setOpaque(false);
	 naviGrandiColpite[i] = naviGrandiColpite[i] + 1; // Aggiorna il numero di caselle colpite di quella specifica nave
	 if (naviGrandiColpite[i] == 2 && (i == 1 || i == 2 || i == 3)) { // Controlla la tipologia della nave grazie all'ID e se e' stata affondata
	  feed3.setText("Il Giocatore ha affondato una nave da 2!");
	  feed3.setOpaque(true);
      liberaSpazio(grigliaComputer, riga, colonna);	  }
	 else if (naviGrandiColpite[i] == 3 && (i == 4 || i == 5)) {
	  feed3.setText("Il Giocatore ha affondato una nave da 3!");
	  feed3.setOpaque(true);
      liberaSpazio(grigliaComputer, riga, colonna); }
	 else if (naviGrandiColpite[i] == 4 && i == 6) {
	  feed3.setText("Il Giocatore ha affondato una nave da 4!"); 
	  feed3.setOpaque(true);
	  liberaSpazio(grigliaComputer, riga, colonna); }
	 return true; } }
   grigliaComputer[riga][colonna].setBackground(Color.WHITE); // Il giocatore non colpisce nulla
   feed2.setText("Il Giocatore ha mancato il bersaglio!");
   feed2.setOpaque(true);
   feed3.setText(" ");
   feed3.setOpaque(false);
   return false; } }
 
 public void gameOver(boolean vinto) { // Il gioco e' terminato
  String vittoria; // Stampa su schermo il nome del vincitore
  if (vinto == true) {
   vittoria = "Vince il Giocatore!"; }
  else {
   vittoria = "Vince il Computer!"; }
   
  String[] opzioni = {"Nuova Partita", "Esci"}; // Chiede al giocatore se giocare nuovamente
  int scelta = JOptionPane.showOptionDialog(null, new JLabel(vittoria, JLabel.CENTER), "BATTAGLIA NAVALE - Vs CPU", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, opzioni, opzioni[0]);
  if (scelta == 0) {
   Gioco gioco = new Gioco();
   gioco.setSize(240, 625);
   gioco.setResizable(false);
   gioco.setVisible(true);
   gioco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
   setVisible(false);
   dispose(); }
  else {
   setVisible(false);
   dispose(); } } } 
