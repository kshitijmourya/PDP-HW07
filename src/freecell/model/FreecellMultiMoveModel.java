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

  public LinkedList<Cards> getMultipleCardsToMove(List<LinkedList<Cards>> pilesInput, int pileNumber, int cardValue) {
    LinkedList<Cards> shifting_cards = new LinkedList<Cards>();
    int shifting_card_value = 0;
    int count = 0;

    // check if chosen pile is empty
    if (!pilesInput.get(pileNumber).isEmpty()) {
      // find value of first card matches cardIndex given by user.
      for (Cards c : pilesInput.get(pileNumber)) {
        // count to find index of desired card.
        count++;
        // get value of card in chosen pile
        shifting_card_value = value_table.get(c.getValue());
        // if value of card matches the value wanted.
        if (shifting_card_value == cardValue) {
          break;
        }

        shifting_card_value = 0;
      }

      if (shifting_card_value > 0) {
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

        int cardLimit = (int) ((numberOpen + 1) * Math.pow(2, numberCascade));
        if (shifting_cards.size() > cardLimit) {
          shifting_cards = new LinkedList<Cards>();
        } else {
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
      Cards card_shifting;
      LinkedList<Cards> multiple_cards = new LinkedList<>();

      if (source == PileType.CASCADE) {
        // System.out.println("here");
        List<LinkedList<Cards>> test_piles = new ArrayList<>();
        test_piles.addAll(this.cascadePiles.getPiles());

        multiple_cards = getMultipleCardsToMove(test_piles, pileNumber, cardIndex);

        if (destination.equals(PileType.CASCADE)) {
          while (!multiple_cards.isEmpty()) {
            card_shifting = multiple_cards.poll();
            putInCascade(source, pileNumber, destPileNumber, card_shifting, value_table.get(card_shifting.getValue()));
          }
        }

        if (destination.equals(PileType.FOUNDATION) && multiple_cards.size() == 1) {
          card_shifting = multiple_cards.get(0);
          putInFoundation(source, pileNumber, destPileNumber, card_shifting, value_table.get(card_shifting.getValue()));
        }

        if (destination.equals(PileType.OPEN) && multiple_cards.size() == 1) {
          //System.out.println("destination is OPEN");
          card_shifting = multiple_cards.get(0);
          //System.out.println(card_shifting);
          putInOpen(source, pileNumber, destPileNumber, card_shifting);
        }

      } else {
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
  }
}
