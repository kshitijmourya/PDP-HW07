package freecell.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents a model for a game of freecell that allows the player to move
 * multiple cards at once.
 */
public class FreecellMultiMoveModel extends FreecellModelAbstract {

  public static FreecellMultiMoveModelBuilder getBuilder() {
    return new FreecellMultiMoveModelBuilder();
  }

  /**
   * Constructor for the freecell model that allows multiple moves.
   *
   * @param cascades number of cascade piles.
   * @param opens number of open piles.
   * @throws IllegalArgumentException if the piles fall below the minimum threshhold.
   */
  private FreecellMultiMoveModel(int cascades, int opens) throws IllegalArgumentException {
    super(cascades, opens);
  }

  /**
   * Inner class for the builder that helps build the constructor for the freecell model that
   * allows multiple moves.
   */
  public static class FreecellMultiMoveModelBuilder implements FreecellOperationsBuilder {
    private int opensPiles;
    private int cascadesPiles;

    /**
     * Setting the default values for the multiple move freecell model builder.
     */
    private FreecellMultiMoveModelBuilder() {
      opensPiles = 4;
      cascadesPiles = 8;
    }


    @Override
    public FreecellOperationsBuilder cascades(int c) {
      cascadesPiles = c;
      return this;
    }

    @Override
    public FreecellOperationsBuilder opens(int o) {
      opensPiles = o;
      return this;
    }

    @Override
    public <K> FreecellOperations<K> build() {
      return new FreecellMultiMoveModel(cascadesPiles, opensPiles);
    }
  }

  /**
   * Method which gets multiple cards to be moved as a list. It checks if the cards in the list
   * alternate in color and have the correct value order.
   *
   * @param pilesInput copy of piles in pile source type.
   * @param pileNumber the pile index number.
   * @param cardValue  the index of the highest valued card in the cascade to be moved.
   * @return list of cards to be moved.
   */
  public LinkedList<Cards> getMultipleCardsToMove(List<LinkedList<Cards>> pilesInput,
                                                  int pileNumber, int cardValue) {
    LinkedList<Cards> shifting_cards = new LinkedList<Cards>();
    // check if chosen pile is empty
    if (!pilesInput.get(pileNumber).isEmpty()) {

      // check if there is enough empty piles to move cards.
      // open spaces needed = (open + 1) * 2^cascade.
      int numberOpen = 0;
      int numberCascade = 0;
      for (LinkedList<Cards> p : this.openPiles.getPiles()) {
        if (p.size() == 0) {
          numberOpen++;
        }
      }
      for (LinkedList<Cards> c : this.cascadePiles.getPiles()) {
        if (c.size() == 0) {
          numberCascade++;
        }
      }


      //int cardLimit = (numberOpen + 1) + numberCascade * numberOpen;
      double cardLimit = (numberOpen + 1) * Math.pow(2, numberCascade);
      //if (cardValue > cardLimit) {
      //shifting_cards = new LinkedList<Cards>();
      //} else {

      int start = pilesInput.get(pileNumber).size() - 1;
      for (int i = start; i >= start - cardValue; i--) {
        shifting_cards.addLast(pilesInput.get(pileNumber).get(i));
      }
      //System.out.println(start);
      //System.out.println(cardValue);
      //System.out.println(pilesInput.get(pileNumber).get(start - cardValue));
      // check if card colors alternate, and if cards are ordered in same number
      // if they are then keep list as is, else make list empty.
      /*
      try { //empty

      } catch (IndexOutOfBoundsException e) {
        throw new IndexOutOfBoundsException(shifting_cards.toString());
      } */
      //CardColor checkColor = shifting_cards.get(0).getColor();

      if (shifting_cards.size() > 0) {
        CardColor checkColor = shifting_cards.get(0).getColor();
        int checkValue = value_table.get(shifting_cards.get(0).getValue());

        for (Cards c : shifting_cards) {
          if (shifting_cards.indexOf(c) == 0) {
            //empty block
          } else if (c.getColor() == checkColor) {
            shifting_cards = new LinkedList<Cards>();
            break;
          } else if (value_table.get(c.getValue()) - checkValue != 1) {
            //System.out.println(c.getValue());
            shifting_cards = new LinkedList<Cards>();
            break;
          } else {
            checkColor = c.getColor();
            checkValue = value_table.get(c.getValue());
          }
        }
      }

    }
    return shifting_cards;
  }

  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid.
   * Does so by using helper functions to obtain copies of the card/cards to be moved. And helper
   * functions to apply the move and remove the card/cards from the source, only if valid.
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
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {


    if (this.isGameOver()) {
      throw new IllegalStateException("Game is Over");
    }
    if (!hasGameBegun) {
      throw new IllegalStateException("Game has not begun");
    } else {
      // ArrayList<LinkedList<Cards>> cards = new ArrayList<>();
      //for (int i = cardIndex; i < this.cascadePiles.getPiles().size(); i++) {
      // cards.add(this.cascadePiles.getPiles().get(i));
      //}
      int numberOpen = 0;
      int numberCascade = 0;


      for (LinkedList<Cards> p : this.openPiles.getPiles()) {
        if (p.size() == 0) {
          numberOpen++;
        }
      }
      for (LinkedList<Cards> c : this.cascadePiles.getPiles()) {
        if (c.size() == 0) {
          numberCascade++;
        }
      }

      int cardLimit = (int) ((numberOpen + 1) * Math.pow(2, numberCascade));
      //System.out.println(cardLimit);

      LinkedList<Cards> multiple_cards = new LinkedList<>();
      List<LinkedList<Cards>> test_piles2 = new ArrayList<>();
      test_piles2.addAll(this.cascadePiles.getPiles());
      multiple_cards = getMultipleCardsToMove(test_piles2, pileNumber, cardIndex);

      //System.out.println(multiple_cards.size());


      if (multiple_cards.size() <= cardLimit) {
        Cards card_shifting;
        //LinkedList<Cards> multiple_cards = new LinkedList<>();

        if (source == PileType.CASCADE) {
          List<LinkedList<Cards>> test_piles = new ArrayList<>();
          test_piles.addAll(this.cascadePiles.getPiles());

          if (cardIndex > this.cascadePiles.getPiles().get(pileNumber).size()) {
            throw new IllegalArgumentException("Invalid Card Index");
          }

          //multiple_cards = getMultipleCardsToMove(test_piles, pileNumber, cardIndex);
          //System.out.println(multiple_cards);

          //System.out.println(multiple_cards);
          if (destination.equals(PileType.CASCADE)) {
            while (!multiple_cards.isEmpty()) {
              card_shifting = multiple_cards.pollLast();
              // System.out.println(card_shifting);
              putInCascade(source, pileNumber, destPileNumber,
                      card_shifting, value_table.get(card_shifting.getValue()));
            }
          }

          if (destination.equals(PileType.FOUNDATION) && multiple_cards.size() == 1) {

            card_shifting = multiple_cards.get(0);
            putInFoundation(source, pileNumber, destPileNumber,
                    card_shifting, value_table.get(card_shifting.getValue()));

          }


          if (destination.equals(PileType.OPEN) && multiple_cards.size() == 1) {

            card_shifting = multiple_cards.get(0);
            putInOpen(source, pileNumber, destPileNumber, card_shifting);
          }

        } else {

          card_shifting = getCardToMove(source, pileNumber, cardIndex);

          if (!card_shifting.getValue().equals("none")) {
            int shifting_card_value = value_table.get(card_shifting.getValue());
            // if destination is open pile, make sure it is empty.
            if (destination.equals(PileType.OPEN)) {
              //System.out.println("here");
              if (this.openPiles.getPiles().isEmpty()) {
                putInOpen(source, pileNumber, destPileNumber, card_shifting);
              }
            }

            if (destination.equals(PileType.FOUNDATION)) {
              putInFoundation(source, pileNumber,
                      destPileNumber, card_shifting, shifting_card_value);
            }

            if (destination.equals(PileType.CASCADE)) {
              putInCascade(source, pileNumber, destPileNumber, card_shifting, shifting_card_value);
            }
          }
        }
      } else {
        //empty
      }
    }

  }

  /**
   * Helper method that will take in the card to be placed into an open pile. If the pile is empty,
   * it will place the card and then remove it from the source. If the pile is not empty it will
   * not place the card at the destination and not remove the card from the source.
   *
   * @param source         source pile type.
   * @param pileNumber     pile index number.
   * @param destPileNumber index number for the destination pile.
   * @param cardShifting   copy of card that is shifting.
   */
  public void putInOpen(PileType source, int pileNumber,
                        int destPileNumber, Cards cardShifting) {
    if (this.openPiles.getPiles().get(destPileNumber).isEmpty()) {
      this.openPiles
              .getPiles()
              .get(destPileNumber)
              .addFirst(cardShifting);
      removeCard(source, pileNumber);
    }
  }

  /**
   * Helper method that will take in the card to be placed into a cascade pile. If the pile is empty,
   * it will place the card and then remove it from the source. If the pile is not empty it will
   * check if the card can be moved legally at the destination, then place it in the destination and
   * remove the card from the source if legal.
   *
   * @param source         source pile type.
   * @param pileNumber     pile index number.
   * @param destPileNumber index number for the destination pile.
   * @param cardShifting   copy of card that is shifting.
   * @param shiftingCardValue shifting card value.
   */
  public void putInCascade(PileType source, int pileNumber,
                           int destPileNumber, Cards cardShifting, int shiftingCardValue) {
    //Check for card at destination.
    //compare both cards.
    //Allow the move only when conditions are fulfilled.
    //Colour should be opposite and value should be one less than the card present there.
    //Otherwise it is an invalid move.

    //1) Check if pile is empty. Move the card.
    if (this.cascadePiles.getPiles().get(destPileNumber).isEmpty()) {
      this.cascadePiles.getPiles().get(destPileNumber).addLast(cardShifting);
      removeCard(source, pileNumber);
    }

    //If pile is not empty. Compare the card at destination pile with shifting card.
    //They should have different colours.
    //Value should be one less than the card present.
    else if (!this.cascadePiles.getPiles().get(destPileNumber).isEmpty()
            && shiftingCardValue -
            value_table
                    .get(this.cascadePiles.getPiles()
                            .get(destPileNumber)
                            .peekLast()
                            .getValue()) == -1
            && !cardShifting.getColor().equals(
            this.cascadePiles
                    .getPiles()
                    .get(destPileNumber)
                    .peekLast()
                    .getColor())) {
      this.cascadePiles.getPiles().get(destPileNumber).addLast(cardShifting);
      removeCard(source, pileNumber);
    } else {
      //throw new IllegalArgumentException("Invalid Move");
    }

  }

  /**
   * Helper method that will take in the card to be placed into a foundation pile. If the pile is empty,
   * it will place the card and then remove it from the source. If the pile is not empty it will
   * check if the card can be moved legally at the destination, then place it in the destination and
   * remove the card from the source if legal.
   *
   * @param source         source pile type.
   * @param pileNumber     pile index number.
   * @param destPileNumber index number for the destination pile.
   * @param cardShifting   copy of card that is shifting.
   * @param shiftingCardValue shifting card value.
   */
  public void putInFoundation(PileType source, int pileNumber,
                              int destPileNumber, Cards cardShifting, int shiftingCardValue) {

    // if pile is empty and the card being moved is an ACE, then add it to the pile.
    if (this.foundationPiles.getPiles().get(destPileNumber).isEmpty()
            && shiftingCardValue == 1) {
      this.foundationPiles.getPiles().get(destPileNumber).addFirst(cardShifting);
      removeCard(source, pileNumber);
    }
    // if pile is not empty, card value is 1 level higher than current card in pile,
    // and card to be added has the suite matching that of the cards in the pile
    // the add the card.
    else if (!this.foundationPiles.getPiles().get(destPileNumber).isEmpty()
            && shiftingCardValue -
            value_table
                    .get(this.foundationPiles.getPiles()
                            .get(destPileNumber)
                            .peekLast()
                            .getValue()) == 1
            && cardShifting.getColor().equals(
            this.foundationPiles
                    .getPiles()
                    .get(destPileNumber)
                    .peekLast()
                    .getColor())) {
      this.foundationPiles.getPiles().get(destPileNumber).addLast(cardShifting);
      removeCard(source, pileNumber);
    } //else {
    //throw new IllegalArgumentException("Invalid Move");
    //}
  }


}



