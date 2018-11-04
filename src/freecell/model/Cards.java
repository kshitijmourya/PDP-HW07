package freecell.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cards class which gives methods for cards. It implements CardDeck interface and overides its
 * methods.
 */
public class Cards implements CardDeck {
  private String suite;
  private CardColor color;
  private String value;

  /**
   * constructor for the class.
   */
  Cards() {
    //empty constructor.
    /*
     *empty constructor
     */
  }

  /**
   * constructor which initializes the values.
   *
   * @param value value.
   * @param color color of card.
   * @param suite type of suite cards belongs to.
   */
  private Cards(String value, CardColor color, String suite) {
    this.suite = suite;
    this.value = value;
    this.color = color;
  }

  /**
   * gives which suite the card belongs.
   *
   * @return suite name.
   */
  @Override
  public String getSuite() {
    return this.suite;
  }

  /**
   * gives value of the card.
   *
   * @return value of the card.
   */
  @Override
  public String getValue() {
    return this.value;
  }

  /**
   * gives the colour the card.
   *
   * @return color of the card.
   */
  @Override
  public CardColor getColor() {
    return this.color;
  }

  /**
   * creates a new deck.
   *
   * @return list of cards as a deck.
   */
  @Override
  public List<Cards> createDeck() {
    List<String> card_values = Arrays.asList("A", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "J", "Q", "K");
    List<Cards> deck = new ArrayList<>();
    for (int i = 0; i < card_values.size(); i++) {
      deck.add(new Cards(card_values.get(i), CardColor.RED, "♥"));
      deck.add(new Cards(card_values.get(i), CardColor.RED, "♦"));
      deck.add(new Cards(card_values.get(i), CardColor.BLACK, "♠"));
      deck.add(new Cards(card_values.get(i), CardColor.BLACK, "♣"));
    }
    return deck;
  }


  /**
   * tostring method.
   *
   * @return string of value and suite to print.
   */
  @Override
  public String toString() {
    return this.value + this.suite;
  }
}
