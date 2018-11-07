package freecell.model;


import java.util.ArrayList;
import java.util.List;

public final class MockModel implements FreecellOperations {

  private StringBuilder log;
  private final int uniqueCode;


  /**
   * MockModel.
   *
   * @param log        log
   * @param uniqueCode uniquecode
   */
  public MockModel(StringBuilder log, int uniqueCode) {

    this.log = log;
    this.uniqueCode = uniqueCode;

  }

  @Override
  public List getDeck() {

    return new ArrayList<>();
  }

  @Override
  public void startGame(List deck, boolean shuffle) throws IllegalArgumentException {

    System.out.println("Inside mock model startgame");
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    log.append("Input: " + source + " " + pileNumber
            + " " + cardIndex + " " + destination + " " + destPileNumber + "\n");

  }

  @Override
  public boolean isGameOver() {

    return false;
  }

  @Override
  public String getGameState() {

    return uniqueCode + " : ";
  }
}