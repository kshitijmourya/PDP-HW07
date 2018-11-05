package freecell.controller;

import java.util.List;

import freecell.model.FreecellOperations;

public class FreecellController implements IFreecellController<K> {

  public Readable rd;
  public Appendable ap;

  /**
   * @param rd readable object for reading user's inputs
   * @param ap appendable object for transmitting message/game state
   */
  public FreecellController(Readable rd, Appendable ap) {
    this.rd = rd;
    this.ap = ap;
  }


  private boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
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

    if ((deck == null) || (model == null)) {
      throw new IllegalArgumentException("The deck cannot be null.");
    }
    if ((rd == null) || (ap == null)) {
      throw new IllegalStateException("Readable or appendable objects cannot be null.");
    }

    try {
      model.startGame(deck, shuffle);
    } catch (IllegalArgumentException e) {
      //appendTryCatch("Could not start game");
      return;
    }


  }
}
