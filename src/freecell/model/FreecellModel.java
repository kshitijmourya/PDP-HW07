package freecell.model;


/**
 * This class implements FreecellOperations Interface.
 */
public class FreecellModel extends FreecellModelAbstract {

  public static FreecellModelBuilder getBuilder() {
    return new FreecellModelBuilder();
  }

  /**
   * Creates new freecellmodel.
   *
   * @param cascades cascades piles.
   * @param opens    opens piles.
   */
  private FreecellModel(int cascades, int opens) throws IllegalArgumentException {
    super(cascades, opens);
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
      Cards card_shifting;

      card_shifting = getCardToMove(source, pileNumber, cardIndex);

      int shifting_card_value = value_table.get(card_shifting.getValue());

      // if destination is open pile, make sure it is empty.
      if (destination.equals(PileType.OPEN)) {
        putInOpen(source, pileNumber, destPileNumber, card_shifting);
      }

      if (destination.equals(PileType.FOUNDATION)) {
        putInFoundation(source, pileNumber, destPileNumber, card_shifting, shifting_card_value);
      }

      if (destination.equals(PileType.CASCADE)) {
        putInCascade(source, pileNumber, destPileNumber, card_shifting, shifting_card_value);
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
    if (!this.openPiles.getPiles().get(destPileNumber).isEmpty()) {
      throw new IllegalArgumentException("Pile is not empty");
    } else {
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
      throw new IllegalArgumentException("Invalid Move");
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
      removeCard(source, pileNumber);
      this.foundationPiles.getPiles().get(destPileNumber).addLast(cardShifting);
    } //else {
    //throw new IllegalArgumentException("Invalid Move");
    //}
  }
}
