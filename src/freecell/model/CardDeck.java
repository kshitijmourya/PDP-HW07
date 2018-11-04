package freecell.model;

import java.util.List;

/**
 * CardDeck interface which has various methods. This interface gives all the methods when a deck.
 * is created. It could be used to access all deck related operations.
 */
public interface CardDeck {

  /**
   * It returns the value of that deck.
   *
   * @return Value of the cardDeck.
   */
  String getValue();

  /**
   * It returns the colour determined in the enum class. It could be either Red or Black.
   *
   * @return colour of the deck.
   */
  CardColor getColor();

  /**
   * It returns the suit of the deck. It could be any of the spades, clubs, diamonds or hearts.
   *
   * @return Suite of the deck.
   */
  String getSuite();

  /**
   * It creates a brand new deck of the given cards.
   *
   * @return a new deck in list form.
   */
  List<Cards> createDeck();

  String toString();
}
