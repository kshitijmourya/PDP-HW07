package freecell.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements FreecellOperations Interface.
 */
public class FreecellModel extends FreecellModelAbstract {

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

      card_shifting = getCard(source, pileNumber, cardIndex);

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
}
