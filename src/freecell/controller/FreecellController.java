package freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import freecell.model.FreecellOperations;
import freecell.model.PileType;


public class FreecellController<K> implements IFreecellController<K> {

  public Readable rd;
  public Appendable ap;

  /**
   * @param rd
   * @param ap
   */
  public FreecellController(Readable rd, Appendable ap) {
    this.rd = rd;
    this.ap = ap;
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
      throw new IllegalArgumentException("The model cannot tbe null");
    }
    if ((rd == null) || (ap == null)) {
      throw new IllegalStateException("Readable or appendable objects cannot be null.");
    }

    try {
      model.startGame(deck, shuffle);
    } catch (IllegalArgumentException e) {
      appendHelper("Unable to start game.");
      e.printStackTrace();
    }
    Scanner scan = new Scanner(this.rd);
    ArrayList<String> userInput = new ArrayList<String>();
    appendHelper(model.getGameState());

    while (scan.hasNext()) {

      String s = scan.next();

      // checks if the input is 'q' or 'Q'
      if (s.startsWith("q") || s.startsWith("Q")) {
        appendHelper("\n" + "Game quit prematurely.");
        return;
      }

      // checks card index, source pile and destination pile here

      if (userInput.size() == 1) {
        try {
          if (isValidCardIndex(s)) {
            userInput.add(s);
          }
        } catch (IllegalArgumentException e) {
          appendHelper("\n" + e.getMessage());
        }
      } else if ((userInput.size() == 0) || (userInput.size() == 2)) {
        try {
          if (isValidPile(s)) {
            userInput.add(s);
          }
        } catch (IllegalArgumentException e) {
          if (userInput.size() == 2) {
            appendHelper("\n" + "Invalid destination pile. Try again.");
          } else {
            appendHelper("\n" + "Invalid source pile. Try again.");
          }
        }
      }

      // Once the userInput contains all the valid inputs for a move, come here.
      if (userInput.size() == 3) {
        PileType sourcePile = getPileType(userInput.get(0));
        int sourcePileIndex = getIndex(userInput.get(0));
        int cardIndex = getIndex(userInput.get(1));
        PileType destPile = getPileType(userInput.get(2));
        int destPileIndex = getIndex(userInput.get(2));

        try {
          model.move(sourcePile, sourcePileIndex - 1, cardIndex - 1, destPile,
                  destPileIndex - 1);
        } catch (IllegalArgumentException e) {
          appendHelper("\n" + e.getMessage());
        }

        appendHelper("\n" + model.getGameState());
        userInput.clear();
      }
    }

    if (model.isGameOver()) {
      appendHelper("\n" + "Game over.");
      return;
    }

    return;
  }


  private void appendHelper(String s) {
    try {
      ap.append(s);
    } catch (IOException o) {
      o.printStackTrace();
    }
  }

  private boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }


  private boolean isValidPile(String c) {
    String firstChar = c.substring(0, 1);
    String index = c.substring(1);
    if (!(firstChar.equals("C") || firstChar.equals("F") || firstChar.equals("O"))) {
      throw new IllegalArgumentException();
    } else {
      try {
        Integer.parseInt(index);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid pile. Try again.");
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("The pile needs to be followed by number. Try again.");
      }
    }
    // only got here if we didn't return false
    return true;
  }

  private boolean isValidCardIndex(String c) {
    try {
      return Integer.parseInt(c) >= 1;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("\n" + "Invalid card index. Try again.");
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("The card index cannot be null. Try again.");
    }
  }


  private PileType getPileType(String c) {
    String firstChar = c.substring(0, 1);
    PileType result = null;
    switch (firstChar) {
      case "C":
        result = PileType.CASCADE;
        break;
      case "F":
        result = PileType.FOUNDATION;
        break;
      case "O":
        result = PileType.OPEN;
        break;
      default:
        throw new IllegalArgumentException("Invalid pile.");
    }
    return result;
  }

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


