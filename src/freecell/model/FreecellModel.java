package freecell.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements FreecellOperations Interface.
 */
public class FreecellModel implements FreecellOperations {
  private CardDeck deck_of_cards;
  private Piles openPiles;
  private Piles cascadePiles;
  private Piles foundationPiles;
  private boolean hasGameBegun = false;
  private int count;
  private int opens;
  private int cascades;

  private final HashMap<String, Integer> value_table = new HashMap<String, Integer>() {
    {
      put("A", 1);
      put("2", 2);
      put("3", 3);
      put("4", 4);
      put("5", 5);
      put("6", 6);
      put("7", 7);
      put("8", 8);
      put("9", 9);
      put("10", 10);
      put("J", 11);
      put("Q", 12);
      put("K", 13);
    }
  };

  public static FreecellModelBuilder getBuilder() {

    return new FreecellModelBuilder();
  }

  /**
   * creates new freecellmodel.
   *
   * @param cascades cascades piles.
   * @param opens    opens piles.
   */
  private FreecellModel(int cascades, int opens) throws IllegalArgumentException {
    this.deck_of_cards = new Cards();
    this.opens = opens;
    this.cascades = cascades;

    if (opens < 1 || cascades < 4) {
      throw new IllegalArgumentException("Minimum Open piles should be 1 and " +
              "cascade piles should be 4");
    }

    if (opens > 1000 || cascades > 1000) {
      throw new IllegalArgumentException("Maximum Size limit reached according to implementation");
    }


    this.openPiles = new Piles(opens, PileType.OPEN);
    this.cascadePiles = new Piles(cascades, PileType.CASCADE);
    this.foundationPiles = new Piles(4, PileType.FOUNDATION);
  }

  public static class FreecellModelBuilder implements FreecellOperationsBuilder {

    private int opensPiles;
    private int cascadesPiles;

    private FreecellModelBuilder() {

      opensPiles = 4;
      cascadesPiles = 8;
    }

    @Override
    public FreecellOperationsBuilder opens(int opens) {

      opensPiles = opens;
      return this;
    }

    @Override
    public FreecellOperationsBuilder cascades(int cascades) {

      cascadesPiles = cascades;
      return this;
    }


    @Override
    public <K> FreecellOperations<K> build() {

      return new FreecellModel(cascadesPiles, opensPiles);
    }

  }

  /**
   * Return a valid and complete deck of cards for a game of Freecell. There is no restriction
   * imposed on the ordering of these cards in the deck. An invalid deck is defined as a deck that
   * has one or more of these flaws:
   * <ul>
   * <li>It does not have 52 cards</li> <li>It has duplicate cards</li> <li>It
   * has at least one invalid card (invalid suit or invalid number) </li> </ul>
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Cards> getDeck() {
    return deck_of_cards.createDeck();
  }

  /**
   * Deal a new game of freecell with the given deck, with or without shuffling it first. This
   * method first verifies that the deck is valid. It deals the deck among the cascade piles in
   * roundrobin fashion. Thus if there are 4 cascade piles, the 1st pile will get cards 0, 4, 8,
   * ..., the 2nd pile will get cards 1, 5, 9, ..., the 3rd pile will get cards 2, 6, 10, ... and
   * the 4th pile will get cards 3, 7, 11, .... Depending on the number of cascade piles, they may
   * have a different number of cards
   *
   * @param deck    the deck to be dealt in the game.
   * @param shuffle if true, shuffle the deck else deal the deck as-is.
   * @throws IllegalArgumentException if the deck is invalid.
   */
  @Override
  public void startGame(List deck, boolean shuffle) throws IllegalArgumentException {

    if (deck.size() == 52) {
      //System.out.println("here1");
      CardDeck new_deck = new Cards();
      Cards[] control_deck = new Cards[52];
      List<Cards> test_deck = new_deck.createDeck();
      test_deck.toArray(control_deck);
      List<String> control_list = Arrays.stream(control_deck)
              .map(a -> a.getValue() + a.getSuite())
              .collect(Collectors.toList());

      Cards[] exp_deck = new Cards[52];
      deck.toArray(exp_deck);

      List<String> exp_list = Arrays.stream(exp_deck)
              .map(a -> a.getValue() + a.getSuite())
              .collect(Collectors.toList());

      if (!exp_list.containsAll(control_list)) {
        throw new IllegalArgumentException("Invalid Deck");
      }
    } else {
      throw new IllegalArgumentException("Invalid Deck");
    }

    /*
     *  what id startgame is called after another game has already been started.
     * we need to reset all values back to the original state after the builder was called....
     * whats the best way to do this?
     */
    if (count > 0) {
      count--;

      this.openPiles = new Piles(opens, PileType.OPEN);
      this.foundationPiles = new Piles(4, PileType.FOUNDATION);
      this.cascadePiles = new Piles(cascades, PileType.CASCADE);
      //this.foundationPiles.getPiles().clear();
      //this.cascadePiles.getPiles().clear();
      //System.out.println("Here");
      this.startGame(deck, shuffle);

    }

    if (count == 0) {
      count = 1;
      List<Cards> copy_deck = new ArrayList<Cards>();
      copy_deck.addAll(deck);

      // if shuffle input is true the shuffle the deck.
      if (shuffle) {
        // Collections.shuffle() method randomly shuffle a Collections object.
        Collections.shuffle(copy_deck);
      }

      // got the number of cascade piles from cascade piles. Could have used this.cascades instead
      // for a simpler approach. However, using it through getPiles.getsize() we ensure that the
      // correct pile number was made when the object was instantiated.
      int number_of_piles = this.cascadePiles.getPiles().size();

      // in the while-loop below, as cards in the deck are being used, they are being removed
      // so the condition here is to keep looping until there are no more cards in the deck.
      while (!copy_deck.isEmpty()) {
        // for each pile in the cascade section,
        // get the top card in the deck and add it to the pile.
        for (int i = 0; i < number_of_piles; i++) {
          // if the deck becomes empty as it is distributing cards then the process of distribution
          // will stop and the loop will break.
          if (!copy_deck.isEmpty()) {
            // had to cast to get working, I don't know what K means in the interface but its
            // keeping me from making the deck as List<Cards>. Ask in office hours.
            this.cascadePiles.getPiles().get(i).addLast(copy_deck.get(0));
            copy_deck.remove(0);
          } else {
            break;
          }
        }
        hasGameBegun = true;
      }
    }
  }

  /**
   * method to getshiftingcard.
   *
   * @param pilesInput input pile type.
   * @param pileNumber input pile number.
   * @param cardValue  the face value of card.
   * @return the card to shift.
   */

  public Cards getShiftingCard(List<LinkedList<Cards>> pilesInput, int pileNumber, int cardValue) {
    Cards shifting_card = new Cards();
    // check if chosen pile is empty
    if (!pilesInput.get(pileNumber).isEmpty()) {
      // get value of first card in chosen pile
      int shifting_card_value = value_table.get(pilesInput.get(pileNumber).peekLast().getValue());
      // check if value of first card matches cardIndex given by user.
      // If yes get card, else reject.
      //if (shifting_card_value == cardValue) {
      shifting_card = pilesInput.get(pileNumber).peekLast();
      //}
      //else {
      //throw new IllegalArgumentException("Here is the exception  " + shifting_card_value);
      //}
    }

    return shifting_card;
  }

  /**
   * method to removeCard.
   *
   * @param source     piletype of source.
   * @param pileNumber pile number of source.
   */
  public void removeCard(PileType source, int pileNumber) {
    if (source.equals(PileType.FOUNDATION)) {
      this.foundationPiles.getPiles().get(pileNumber).pollLast();
    }

    if (source.equals(PileType.OPEN)) {
      this.openPiles.getPiles().get(pileNumber).pollLast();
    }

    if (source.equals(PileType.CASCADE)) {
      this.cascadePiles.getPiles().get(pileNumber).pollLast();
    }
  }

  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if the move is not possible {@link PileType})
   * @throws IllegalStateException    if a move is attempted before the game has starts
   */
  @Override
  public void move(PileType source, int pileNumber,
                   int cardIndex, PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Game is Over");
    }
    if (!hasGameBegun) {
      throw new IllegalStateException("Game has not begun");
    } else {
      Cards card_shifting = new Cards();
      List<LinkedList<Cards>> pile_source = new ArrayList<LinkedList<Cards>>();

      // get card from foundation pile is impossible.
      if (source.equals(PileType.FOUNDATION)) {
        if (this.foundationPiles.getPiles().get(pileNumber).isEmpty()) {
          throw new IllegalArgumentException("Pile is empty");
        }
        pile_source.addAll(this.foundationPiles.getPiles());
        card_shifting = getShiftingCard(pile_source, pileNumber, cardIndex);
      }

      if (source.equals(PileType.OPEN)) {
        if (this.openPiles.getPiles().get(pileNumber).isEmpty()) {
          throw new IllegalArgumentException("Pile is empty");
        }
        pile_source.addAll(this.openPiles.getPiles());
        card_shifting = getShiftingCard(pile_source, pileNumber, cardIndex);
      }

      if (source.equals(PileType.CASCADE)) {
        if (this.cascadePiles.getPiles().get(pileNumber).isEmpty()) {
          throw new IllegalArgumentException("Pile is empty");
        }
        pile_source.addAll(this.cascadePiles.getPiles());
        card_shifting = getShiftingCard(pile_source, pileNumber, cardIndex);
      }

      int shifting_card_value = value_table.get(card_shifting.getValue());

      // if destination is open pile, make sure it is empty.
      if (destination.equals(PileType.OPEN)) {
        if (!this.openPiles.getPiles().get(destPileNumber).isEmpty()) {
          throw new IllegalArgumentException("Pile is not empty");
        } else {
          this.openPiles
                  .getPiles()
                  .get(destPileNumber)
                  .addFirst(card_shifting);
          removeCard(source, pileNumber);
        }
      }

      if (destination.equals(PileType.FOUNDATION)) {

        // if pile is empty and the card being moved is an ACE, then add it to the pile.
        if (this.foundationPiles.getPiles().get(destPileNumber).isEmpty()
                && shifting_card_value == 1) {
          this.foundationPiles.getPiles().get(destPileNumber).addFirst(card_shifting);
          removeCard(source, pileNumber);
        }
        // if pile is not empty, card value is 1 level higher than current card in pile,
        // and card to be added has the suite matching that of the cards in the pile
        // the add the card.
        else if (!this.foundationPiles.getPiles().get(destPileNumber).isEmpty()
                && shifting_card_value -
                value_table
                        .get(this.foundationPiles.getPiles()
                                .get(destPileNumber)
                                .peekLast()
                                .getValue()) == 1
                && card_shifting.getColor().equals(
                this.foundationPiles
                        .getPiles()
                        .get(destPileNumber)
                        .peekLast()
                        .getColor())) {
          removeCard(source, pileNumber);
          this.foundationPiles.getPiles().get(destPileNumber).addLast(card_shifting);
        } else {
          throw new IllegalArgumentException("Invalid Move");
        }
      }

      if (destination.equals(PileType.CASCADE)) {
        //Check for card at destination.
        //compare both cards.
        //Allow the move only when conditions are fulfilled.
        //Colour should be opposite and value should be one less than the card present there.
        //Otherwise it is an invalid move.

        //1) Check if pile is empty. Move the card.
        if (this.cascadePiles.getPiles().get(destPileNumber).isEmpty()) {
          this.cascadePiles.getPiles().get(destPileNumber).addLast(card_shifting);
          removeCard(source, pileNumber);
        }

        //If pile is not empty. Compare the card at destination pile with shifting card.
        //They should have different colours.
        //Value should be one less than the card present.
        else if (!this.cascadePiles.getPiles().get(destPileNumber).isEmpty()
                && shifting_card_value -
                value_table
                        .get(this.cascadePiles.getPiles()
                                .get(destPileNumber)
                                .peekLast()
                                .getValue()) == -1
                && !card_shifting.getColor().equals(
                this.cascadePiles
                        .getPiles()
                        .get(destPileNumber)
                        .peekLast()
                        .getColor())) {
          this.cascadePiles.getPiles().get(destPileNumber).addLast(card_shifting);
          removeCard(source, pileNumber);
        } else {
          throw new IllegalArgumentException("Invalid Move");
        }
      }
    }
  }

  /**
   * Signal if the game is over or not.
   *
   * @return true if game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    boolean value = false;

    for (int i = 0; i < foundationPiles.getPiles().size(); i++) {
      if (this.foundationPiles.getPiles().get(i).size() == 13) {
        for (int j = 0; j < openPiles.getPiles().size(); j++) {
          if (this.openPiles.getPiles().get(i).size() == 0) {
            for (int k = 0; k < cascadePiles.getPiles().size(); k++) {
              if (this.cascadePiles.getPiles().get(k).size() == 0) {
                value = true;
              }
            }
          }
        }
      } else {
        value = false;
      }
    }

    return value;
  }

  /**
   * Return the present state of the game as a string. The string is formatted as follows:
   * <pre>
   * F1:[b]f11,[b]f12,[b],...,[b]f1n1[n] (Cards in foundation pile 1 in order)
   * F2:[b]f21,[b]f22,[b],...,[b]f2n2[n] (Cards in foundation pile 2 in order)
   * ...
   * Fm:[b]fm1,[b]fm2,[b],...,[b]fmnm[n] (Cards in foundation pile m in
   * order)
   * O1:[b]o11[n] (Cards in open pile 1)
   * O2:[b]o21[n] (Cards in open pile 2)
   * ...
   * Ok:[b]ok1[n] (Cards in open pile k)
   * C1:[b]c11,[b]c12,[b]...,[b]c1p1[n] (Cards in cascade pile 1 in order)
   * C2:[b]c21,[b]c22,[b]...,[b]c2p2[n] (Cards in cascade pile 2 in order)
   * ...
   * Cs:[b]cs1,[b]cs2,[b]...,[b]csps (Cards in cascade pile s in order)
   *
   * where [b] is a single blankspace, [n] is newline. Note that there is no
   * newline on the last line
   * </pre>
   *
   * @return the formatted string as above
   */
  @Override
  public String getGameState() {
    String gameState = "";

    if (hasGameBegun) {
      for (int k = 0; k < this.foundationPiles.getPiles().size(); k++) {
        gameState += helperGameState("F", k, this.foundationPiles.getPiles());
      }
      for (int k = 0; k < this.openPiles.getPiles().size(); k++) {
        gameState += helperGameState("O", k, this.openPiles.getPiles());
      }
      for (int k = 0; k < this.cascadePiles.getPiles().size(); k++) {
        gameState += helperGameState("C", k, this.cascadePiles.getPiles());
      }

    } else {
      gameState = "";

    }

    return gameState.trim();
  }

  /**
   * helper function to get gameState.
   *
   * @param initialString initiail string.
   * @param pileNumber    pile number.
   * @param pilesInput    pile input in list form.
   * @return gives string of gamestate.
   */
  private String helperGameState(String initialString, int pileNumber,
                                 List<LinkedList<Cards>> pilesInput) {
    // starting string is O
    // iteration number gets joined with O
    // the : gets added with a space after
    // then the cards that are there
    // then new space
    StringBuilder build_state = new StringBuilder();
    String pile_state = initialString + String.valueOf(pileNumber + 1) + ":";
    Cards[] pile = new Cards[pilesInput.get(pileNumber).size()];

    pilesInput.get(pileNumber).toArray(pile);
    List<String> state_list = Arrays.stream(pile)
            .map(a -> a.getValue() + a.getSuite())
            .collect(Collectors.toList());

    if (state_list.isEmpty()) {
      return pile_state + "\n";
    }
    for (String state : state_list) {
      if (state.equals(state_list.get(state_list.size() - 1))) {
        build_state.append(" " + state + "\n");
      } else {
        build_state.append(" " + state + ",");
      }
    }

    pile_state += build_state.toString();
    return pile_state;
  }
}
