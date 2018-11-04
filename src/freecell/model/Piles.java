package freecell.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Piles implements CardPiles {
  private PileType type;
  private List<LinkedList<Cards>> piles;

  /**
   * constructor to initialize card piles.
   *
   * @param pileCount count in int.
   * @param type      which type of pile.
   */
  public Piles(int pileCount, PileType type) {
    int pileCount1 = pileCount;
    this.type = type;
    this.piles = new ArrayList<LinkedList<Cards>>();

    for (int i = 0; i < pileCount; i++) {
      this.piles.add(new LinkedList<Cards>());
    }
  }

  /**
   * give the type of pile.
   *
   * @return piletype enum.
   */
  public PileType getType() {
    return this.type;
  }

  /**
   * gives the piles as list.
   *
   * @return list.
   */
  public List<LinkedList<Cards>> getPiles() {
    return this.piles;
  }


}
