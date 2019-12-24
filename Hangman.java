//Jonathan Yuyang Zhou. All rights Reserved. Do not Distribute.

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
public class Hangman extends JFrame {
 private hangman hangmanDrawing;
 private JPanel wordPanel;
 private Wordreadout guessesReadout;
 private JPanel hangmanPanel;
 private JPanel informationPanel;
 private JTextField guessesMade;
 private JTextField nextGuess;
 private HashSet < Character > charsInWord;
 private HashSet < Character > charsGuessSet;
 private JLabel wli;
 private String word;
 private JRadioButton BritishButton;
 private JRadioButton AmericanButton;
 private ButtonGroup LocaleGroup;
 private String localeSpelling = "british-english";
 private int difficulty = 0; // 0 is easy. 1 is medium. 2 is hard.
 private JRadioButton EasyButton;
 private JRadioButton MediumButton;
 private JRadioButton HardButton;
 private ButtonGroup DifficultyGroup;


 // Allow more guesses to be entered
 private boolean CheckActionInputUpdate = true;
 // Number of lines in the dictionary
 static public final int DICT_LINES_BRIT = 99157;
 static public final int DICT_LINES_AMER = 99172;
 /**
  * Hangman Illustration. Also keeps track of number of errors
  */
 private class hangman extends Canvas {
  // How many wrongs
  private int stage = 0;
  private int difficulty; // 0 is easy. 1 is medium. 2 is hard.

  /**
   * Assign th stage of the hangman if the stage is greater than or equal to 12, then the game has ended, and this will  be indicated
   */
  public boolean assignStage(int stage) {

   if (stage >= 11) {
    this.stage = 11;
    return true;
   } else if (stage <= 0) {
    this.stage = 0;
   } else {
    this.stage = stage;
   }
   return false;
  }

  /**
   * Class constuctor, calls superclass constuctor and sets canvas size
   */
  public hangman(int difficulty) {
   super();
   // 0 is easy. 1 is medium. 2 is hard.
   this.setSize(400, 600);
   this.difficulty = difficulty;
   switch (difficulty) {
    case 0:
     this.stage = 0;
     break;
    case 1:
     this.stage = 3;
     break;
    case 2:
     this.stage = 5;
     break;
   }
  }
  /**
   * Draw the hangman.
   */
  public void paint(Graphics g) {

   // The switch statement will draw the given stage
   // and all those under it by falling thourgh (no breaks is intentional)
   g.setColor(Color.BLACK);

   switch (stage) {
    case 11:
     //
     g.drawOval(100, 100, 200, 200);
    case 10:
     g.drawLine(200, 300, 200, 475);
    case 9:
     g.drawLine(200, 475, 125, 550);
    case 8:
     g.drawLine(200, 475, 275, 550);
    case 7:
     g.drawLine(200, 475 - 150, 125, 550 - 150);
    case 6:
     g.drawLine(200, 475 - 150, 275, 550 - 150);
    case 5:
     g.drawLine(200, 75 + 12, 200, 100);// Big veritcal
    case 4:
     g.drawOval(175 + 12, 50 + 12, 25, 25);
    case 3:
     g.drawLine(25, 75, 200 - 12, 75);// Upper fragment
    case 2:
     g.drawLine(50, 575, 50, 50);// Horizontal upper
    case 1:
     g.drawLine(0, 575, 400, 575); //base
   }
  }
 }

 /**
  * Word readout on the bottom of the hangman. Putting it in a class makes life easier
  */
 private class Wordreadout extends JPanel {
  // References to all the Text Fields
  public ArrayList < JTextField > guessesList;
  private String word;

  /**
   * Class constructor, takes in the word
   */
  public Wordreadout(String word) {
   super();
   this.word = word;

   this.guessesList = new ArrayList < JTextField > ();
   // Add each letter to the panel and the list
   for (int i = 0; i < this.word.length(); i++) {
    JTextField textInput = new JTextField(1);
    this.add(textInput);
    this.guessesList.add(textInput);
    textInput.setEditable(false);
   }
  }

  /**
   * Reset this display with a new word.
   */
  public void reset(String word) {
   this.word = word;
   this.removeAll();
   this.guessesList = new ArrayList < JTextField > ();
   // Add each letter to the panel and the list
   for (int i = 0; i < this.word.length(); i++) {
    JTextField textInput = new JTextField(2);
    this.add(textInput);
    this.guessesList.add(textInput);
    textInput.setEditable(false);
    // Redraw
    this.updateUI();
   }
  }


  /**
   * Redraw all the displays.
   */
  public void update(HashSet < Character > charsGuessSet) {
   for (int i = 0; i < this.guessesList.size(); i++) {
    Iterator < Character > iterchars = charsGuessSet.iterator();
    // If the char is an element of the guessed chars
    while (iterchars.hasNext()) {
     if (iterchars.next().charValue() == this.word.charAt(i))
      guessesReadout.guessesList.get(i).setText(Character.toString(this.word.charAt(i)));
    }


   }

  }

  /**
   * Show the word. Reveal the answer
   */
  public void showWord() {
   for (int i = 0; i < this.word.length(); i++) {
    guessesReadout.guessesList.get(i).setText(Character.toString(this.word.charAt(i)));
   }
  }
 }

 /**
  * Class constructor. Sets up GUI Layout, and chooses word
  */
 public Hangman() {

  // Set up JFrame
  super("Hangman");
  this.setSize(700, 700);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setLocationRelativeTo(null);
  this.setResizable(false);
  this.setLayout(new BorderLayout());
  try {
   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } catch (Exception e) {;
  }

  // Declare the two Jpanels in the Frame
  this.informationPanel = new JPanel();
  this.hangmanPanel = new JPanel();

  // Declare the infoPanel Text Fields
  this.guessesMade = new JTextField(30);
  this.guessesMade.setEditable(false);
  this.nextGuess = new JTextField();

  // Handle input when character is inserted
  this.nextGuess.getDocument().addDocumentListener(new DocumentListener() {
   public void insertUpdate(DocumentEvent e) {
    if (CheckActionInputUpdate) {
     handleInput();
    };
   }
   public void removeUpdate(DocumentEvent e) {;
   }
   public void changedUpdate(DocumentEvent e) {;
   }
  });
  // Declare the win-loss indicator
  this.wli = new JLabel("");

  // Get a word to hangman
  this.word = this.getWord();

  // Make a set of all the characters in this word
  Character[] wordcharsobj = new Character[word.length()];
  for (int i = 0; i < word.length(); i++) {
   wordcharsobj[i] = Character.valueOf(word.charAt(i));
  }
  charsInWord = new HashSet < Character > (Arrays.asList(wordcharsobj));

  // Create the Restart button
  JButton restartButton = new JButton("Restart");
  restartButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    restartGame();
   }
  });

  this.BritishButton = new JRadioButton("British");
  this.BritishButton.setSelected(true);
  this.BritishButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    setLocaleSpelling("british-english");
    restartGame();
   }
  });

  this.AmericanButton = new JRadioButton("American");
  this.AmericanButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    setLocaleSpelling("american-english");
    restartGame();
   }
  });


  this.LocaleGroup = new ButtonGroup();
  LocaleGroup.add(this.BritishButton);
  LocaleGroup.add(this.AmericanButton);



  this.EasyButton = new JRadioButton("Easy");
  this.EasyButton.setSelected(true);
  this.EasyButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    //System.out.println("British");
    setDifficulty(0);
    restartGame();
   }
  });

  this.MediumButton = new JRadioButton("Medium");
  this.MediumButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    //System.out.println("American");
    setDifficulty(1);
    restartGame();
   }
  });

  this.HardButton = new JRadioButton("Hard");
  this.HardButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    setDifficulty(2);
    restartGame();
   }
  });
  this.DifficultyGroup = new ButtonGroup();
  this.DifficultyGroup.add(EasyButton);
  this.DifficultyGroup.add(MediumButton);
  this.DifficultyGroup.add(HardButton);


  // Layout all the elements of the infoPanel
  this.informationPanel.setLayout(new BoxLayout(this.informationPanel, BoxLayout.Y_AXIS));
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(new JLabel("Guesses Made"));
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(guessesMade);
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(new JLabel("Your Next Guess"));
  this.informationPanel.add(nextGuess);
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(restartButton);
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(new JLabel("Spelling:"));
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(BritishButton);
  this.informationPanel.add(AmericanButton);
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(new JLabel("Difficulty:"));
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(EasyButton);
  this.informationPanel.add(MediumButton);
  this.informationPanel.add(HardButton);
  this.informationPanel.add(Box.createVerticalStrut(20));
  this.informationPanel.add(wli);
  this.informationPanel.add(Box.createVerticalStrut(20));


  this.guessesMade.setMaximumSize(this.guessesMade.getPreferredSize());
  this.nextGuess.setMaximumSize(this.guessesMade.getPreferredSize());


  // Create a List and Panel for the letters
  this.guessesReadout = new Wordreadout(this.word);

  // Declare a hangman
  this.hangmanDrawing = new hangman(0);
  this.hangmanDrawing.setMinimumSize(new Dimension(400, 600));

  // Add the hangman and the letter panel to the hangman panel
  this.hangmanPanel.add(hangmanDrawing);
  this.hangmanPanel.add(guessesReadout);
  this.add(this.hangmanPanel);

  // Add the things to the JFrame
  this.add(this.hangmanPanel, BorderLayout.CENTER);
  this.add(this.informationPanel, BorderLayout.EAST);

  this.setVisible(true);
  // Create the set of all guessed characters
  this.charsGuessSet = new HashSet < Character > ();

 }

 /**
  * Routine to get random word from the dictonary
  */
 private String getWord() {
  // Choose a word.
  // Words come from the UNIX words file. https://en.wikipedia.org/wiki/Words_(Unix)
  // Dictionary available at https://packages.ubuntu.com/bionic/wamerican
  // Dictionary has had non Basic Latin Alphabet characters replaced with Basic Latin Alphabet ones.
  String wd = "pneumonoultramicroscopicsilicovolcanoconiosis";

  try {
   File dictFile = new File(localeSpelling);

   // Chose a random line in the file (words are newline delimited)
   Random rng = new Random();
   int lineNo = rng.nextInt(DICT_LINES_BRIT);
   // Scan the file until said line is reached
   Scanner dictScanner = new Scanner(dictFile);
   for (int i = 0;
    (i < lineNo) && (dictScanner.hasNext()); i++) {
    wd = dictScanner.nextLine();
   }
  } catch (FileNotFoundException e) {;
  }
  //System.out.println(wd);
  return wd.toLowerCase();
 }
 private void setLocaleSpelling(String locale) {
  this.localeSpelling = locale;
 }
 private void setDifficulty(int i) {
  this.difficulty = i;
 }

 /**
  * Restarts the game. Resets the hangman, the stage, the readout, and word, wordset, and character guess set
  */
 private void restartGame() {
  this.word = this.getWord();
  //System.out.println(this.word);
  Character[] wordcharsobj = new Character[word.length()];
  for (int i = 0; i < word.length(); i++) {
   wordcharsobj[i] = Character.valueOf(word.charAt(i));
  }
  charsInWord = new HashSet < Character > (Arrays.asList(wordcharsobj));

  switch (this.difficulty) {
   case 0:
    this.hangmanDrawing.assignStage(0);
    this.hangmanDrawing.difficulty = 0;
    break;
   case 1:
    this.hangmanDrawing.assignStage(3);
    this.hangmanDrawing.difficulty = 1;
    break;
   case 2:
    this.hangmanDrawing.assignStage(5);
    this.hangmanDrawing.difficulty = 2;
    break;
  }



  this.hangmanDrawing.repaint();
  this.guessesReadout.reset(this.word);
  this.nextGuess.setEditable(true);
  this.charsGuessSet.removeAll(this.charsGuessSet);
  this.wli.setText("");
 }


 /**
  * Input handling method called by the document listener on the input
  */
 private void handleInput() {
  // Get the character
  String rawInput = nextGuess.getText().toLowerCase();
  Character input = rawInput.charAt(rawInput.length() - 1);

  // Assert char is valid
  if (('a' <= input.charValue() && input.charValue() <= 'z') || input.charValue() == '\'') {
   // Add to set
   charsGuessSet.add(input);
   // Get chars from set into string
   StringBuffer disp = new StringBuffer();
   Iterator < Character > iterchars = charsGuessSet.iterator();
   while (iterchars.hasNext()) {
    disp.append(iterchars.next().charValue());
   }
   // Set the guesses made display
   guessesMade.setText(disp.toString());
   guessesReadout.update(this.charsGuessSet);

   // Take a set intersection to get number of wrongs
   HashSet < Character > charsGuessSet2 = new HashSet < Character > (charsGuessSet);
   charsGuessSet2.removeAll(charsInWord);

   // Update the hangman and compute stage
   Boolean terminal;
   switch (this.hangmanDrawing.difficulty) {
   case 1:
	  terminal = this.hangmanDrawing.assignStage(charsGuessSet2.size()+3);
      break;
   case 2:
      terminal = this.hangmanDrawing.assignStage(charsGuessSet2.size()+5);
       break;
   default:
	  terminal = this.hangmanDrawing.assignStage(charsGuessSet2.size());

  }

   this.hangmanDrawing.repaint();

   if (terminal) {
    // You Lost.
    this.nextGuess.setEditable(false);
    this.wli.setText("You Lose!");
    this.guessesReadout.showWord();
   }
  }

  // Check Victory by taking the set difference
  HashSet < Character > charsInWord2 = new HashSet < Character > (charsInWord);
  charsInWord2.removeAll(charsGuessSet);
  // If it is the null set
  if (charsInWord2.size() == 0) {
   // You've won!
   this.nextGuess.setEditable(false);
   this.wli.setText("You Win!");

  }

  // Bad things happen if I try to mutate the widget, so apparently I have to add it to the event queue
  Runnable resetInput = new Runnable() {
   public void run() {
    CheckActionInputUpdate = false;
    nextGuess.setText(input.toString());
    CheckActionInputUpdate = true;
   }
  };
  SwingUtilities.invokeLater(resetInput);
 }

 /**
  * Entry point
  */
 public static void main(String[] args) {
  new Hangman();
 }
}