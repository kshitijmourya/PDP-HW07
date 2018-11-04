package freecell.model;

import java.util.LinkedList;
import java.util.List;

/**
 * CardPile interface. There are 3 types of Piles in Freecell.
 */
public interface CardPiles {

  /**
   * It returns the pileType. It could be open pile, cascade pile or foundation pile.
   */
  PileType getType();

  /**
   * It returns the pile as a linkedlist. It is easier to move around and modify linkedlist.
   *
   * @return Pile in linkedlist structure.
   */
  List<LinkedList<Cards>> getPiles();
}
