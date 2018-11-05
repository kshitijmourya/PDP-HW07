package freecell.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FreecellMultiMoveModel extends FreecellModelAbstract {

  public static FreecellMultiMoveModelBuilder getBuilder() {
    return new FreecellMultiMoveModelBuilder();
  }

  private FreecellMultiMoveModel(int cascades, int opens) throws IllegalArgumentException {
    super(cascades, opens);
  }

  public static class FreecellMultiMoveModelBuilder implements FreecellOperationsBuilder {
    private int opensPiles;
    private int cascadesPiles;

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

  public List<Cards> multiCardMove(List<LinkedList<Cards>> pilesInput, int pileNumber, int cardValue) {
    LinkedList<Cards> shifting_cards = new LinkedList<Cards>();
    int count = 0;

    // check if chosen pile is empty
    if (!pilesInput.get(pileNumber).isEmpty()) {
      // find value of first card matches cardIndex given by user.
      for (Cards c : pilesInput.get(pileNumber)) {
        // count to find index of desired card.
        count++;
        // get value of card in chosen pile
        int shifting_card_value = value_table.get(c.getValue());
        // if value of card matches the value wanted.
        if (shifting_card_value == cardValue) {
          break;
        }
      }

      for (int i = count - 1; i < pilesInput.get(pileNumber).size(); i++) {
        shifting_cards.addLast(pilesInput.get(pileNumber).get(i));
      }

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
      int cardLimit = (int) ((numberOpen + 1) * Math.pow(2,numberCascade));
      if (shifting_cards.size() > cardLimit) {
        shifting_cards = new LinkedList<Cards>();
      }
      // check if card colors alternate, and if cards are ordered in same number
      // if they are then keep list as is, else make list empty.
      CardColor checkColor = shifting_cards.get(0).getColor();
      int checkValue = value_table.get(shifting_cards.get(0).getValue());

      for (Cards c : shifting_cards) {
        if (shifting_cards.indexOf(c) == 0) {

        } else if (c.getColor() == checkColor) {
          shifting_cards = new LinkedList<Cards>();
          break;
        } else if (value_table.get(c.getValue()) - checkValue != -1) {
          shifting_cards = new LinkedList<Cards>();
          break;
        } else {
          checkColor = c.getColor();
          checkValue = value_table.get(c.getValue());
        }
      }
    }

    return shifting_cards;
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
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Game is Over");
    }

    if (!hasGameBegun) {
      throw new IllegalStateException("Game has not begun");
    } else {
      Cards card_shifting = new Cards();
      List<LinkedList<Cards>> pile_source = new ArrayList<LinkedList<Cards>>();

      // get card from foundation pile is possible.
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
}
