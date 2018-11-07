package freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import freecell.model.FreecellOperations;
import freecell.model.PileType;

/**
 * Freecell controller class which implements the given interface. It would allow to handle various
 * models of the game.
 *
 * @param <K> Parameter of type K.
 */
public class FreecellController<K> implements IFreecellController<K> {

  public Readable rd;
  public Appendable ap;

  /**
   * Constructor for the controller.
   *
   * @param rd readable object.
   * @param ap appendable object.
   */
  public FreecellController(Readable rd, Appendable ap) {
    if ((rd == null) || (ap == null)) {
      throw new IllegalArgumentException("Readable/Appendable objects are null");
    }
    this.rd = rd;
    this.ap = ap;
  }

  private void appendHelper(String s) {
    try {
      ap.append(s);
    } catch (IOException o) {
      o.printStackTrace();
    }
  }

  /**
   * Start and play a new game of freecell with the provided deck. This deck should be used as-is.
   * This method returns only when the game is over (either by winning or by quitting)
   *
   * @param deck    the deck to be used to play this game
   * @param model   the model for the game
   * @param shuffle shuffle the deck if true, false otherwise
   * @throws IllegalArgumentException if the deck is null or invalid, or if the model is null
   * @throws IllegalStateException    if the controller is unable to read input or transmit output
   */
  @Override
  public void playGame(List<K> deck, FreecellOperations<K> model, boolean shuffle)
          throws IllegalArgumentException, IllegalStateException {

    if (deck == null) {
      throw new IllegalArgumentException("The deck cannot be null.");
    }
    if (model == null) {
      throw new IllegalArgumentException("The model cannot be null");
    }
    if ((rd == null) || (ap == null)) {
      throw new IllegalStateException("Readable or appendable objects cannot be null.");
    }


    try {
      model.startGame(deck, shuffle);
    } catch (IllegalArgumentException e) {
      appendHelper("Unable to start game.");
      //e.printStackTrace();
    }

    appendHelper(model.getGameState());
    Scanner scan = new Scanner(this.rd);
    ArrayList<String> userInput = new ArrayList<String>();
    if (model.isGameOver()) {
      appendHelper("\n" + "Game over.");
    }

    while (scan.hasNext()) {
      String s = scan.next();
      if (s.startsWith("Q") || s.startsWith("q")) {
        appendHelper("\n" + "Game quit prematurely.");
        return;
      }
      int inputSize = userInput.size();
      switch (inputSize) {

        case 0:
          try {
            if (model.isGameOver()) {
              appendHelper("\n" + "Game over.");
            } else if (isPileValid(s)) {
              userInput.add(s);
            }
          } catch (IllegalArgumentException e) {
            appendHelper("\n" + "Enter a valid source pile");
          }
          break;

        case 1:
          try {
            if (isCardIndexValid(s)) {
              userInput.add(s);
            }
          } catch (IllegalArgumentException e) {
            appendHelper("\n" + e.getMessage());
          }
          break;


        case 2:
          try {
            if (isPileValid(s)) {
              userInput.add(s);
            }
          } catch (IllegalArgumentException e) {
            appendHelper("\n" + "Enter a valid destination pile:");
          }
          break;

        case 3:
          PileType sourcePile = getPileType(userInput.get(0));
          int sourcePileIndex = getIndex(userInput.get(0));
          int cardIndex = getIndex(userInput.get(1));
          PileType destinationPile = getPileType(userInput.get(2));
          int destinationPileIndex = getIndex(userInput.get(2));

          try {
            model.move(sourcePile, sourcePileIndex - 1,
                    cardIndex - 1, destinationPile, destinationPileIndex - 1);
          } catch (Exception e) {
            appendHelper("\n" + e.getMessage());
            e.printStackTrace();
            System.out.println(e);
          }

          appendHelper("\n" + model.getGameState());
          userInput.clear();
          break;
        /**
         * default case.
         */
        default:
          throw new IllegalStateException("Error");

      }
      if (model.isGameOver()) {
        appendHelper("\n" + "Game over.");
      }
    }

    if (model.isGameOver()) {
      appendHelper("\n" + "Game over.");
    }

  }

  /**
   * Returns true if the string can is a Integers which could be parsed.
   *
   * @param s string.
   * @return boolean answer
   */
  private boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * helper function to check whether pile is valid or not.
   *
   * @param s string
   * @return boolean answer.
   */
  private boolean isPileValid(String s) {
    String letter = s.substring(0, 1);
    String index = s.substring(1);

    if (!(letter.equals("F") || letter.equals("O") || letter.equals("C"))) {
      throw new IllegalArgumentException();
    } else {
      try {
        Integer.parseInt(index);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid pile. Try again.");
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("No Pile number given. Try again");
      }
    }
    return true;
  }

  /**
   * helper function to check whether index is valid or not.
   *
   * @param c string c.
   * @return boolean answer
   */
  private boolean isCardIndexValid(String c) {
    try {
      return Integer.parseInt(c) >= 1;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("\n" + "Enter a valid card index:");
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("The card index cannot be null. Try again.");
    }
  }

  /**
   * helper function which returns the piletype from the string. It substrings the first part of the
   * string and checks with various cases.
   *
   * @param c string c.
   * @return piletype.
   */
  private PileType getPileType(String c) {
    String letter = c.substring(0, 1);
    PileType pile = null;

    switch (letter) {
      case "C":
        pile = PileType.CASCADE;
        break;
      case "F":
        pile = PileType.FOUNDATION;
        break;
      case "O":
        pile = PileType.OPEN;
        break;
      default:
        throw new IllegalArgumentException();
    }
    return pile;

  }

  /**
   * helper function which gets the index from the string.
   *
   * @param c string.
   * @return index in int form.
   */
  private int getIndex(String c) {
    String index;
    int result;

    if (isInteger(Character.toString(c.charAt(0)))) {
      index = c;
    } else {
      index = c.substring(1);
    }
    result = Integer.parseInt(index);
    return result;
  }

}


