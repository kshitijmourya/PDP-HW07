import org.junit.Before;
import org.junit.Test;

import java.util.List;

import freecell.model.Cards;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;


public class FreecellModelTest {
  private FreecellOperations testModel;
  private FreecellOperations testModel2;
  private FreecellOperations testModel3;
  private String initialGameState = "F1:\n" +
          "F2:\n" +
          "F3:\n" +
          "F4:\n" +
          "O1:\n" +
          "O2:\n" +
          "O3:\n" +
          "O4:\n" +
          "C1: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
          "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
          "C3: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
          "C4: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
          "C5: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
          "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
          "C7: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
          "C8: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣";


  /**
   * setup method which calls the constructors.
   */
  @Before
  public void setUp() {
    testModel = FreecellModel
            .getBuilder()
            .build();

    testModel2 = FreecellModel
            .getBuilder()
            .cascades(4)
            .opens(2)
            .build();

    testModel3 = FreecellModel.getBuilder().cascades(52).opens(4).build();
  }

  @Test
  public void iniTest() {
    assertEquals("", testModel2.getGameState());

    testModel2.startGame(testModel2.getDeck(), true);
    assertEquals(52, testModel2.getDeck().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void iniTestFail() {
    List<Cards> cards = testModel2.getDeck();
    Cards extra_card = cards.get(1);
    cards.add(extra_card);
    cards.remove(4);
    //System.out.println(cards);
    assertEquals("", testModel2.getGameState());
    testModel.startGame(cards, false);
    //System.out.println(testModel2.getGameState());
  }

  /**
   * Test for getting valid deck. getdeck() method. 1) It should not be empty. 2) Assert right
   * number of cards. 3) No duplicate cards.
   */
  @Test
  public void testGetDeck() {
    List deck = testModel.getDeck();
    assertEquals(52, testModel.getDeck().size());

    //testModel.startGame(deck, false);
  }

  /**
   * Test for startGame() method. Verify that deck is valid. java.lang.IllegalArgumentException - if
   * the deck is invalid test1- With shuffling test 2 - Without shuffling.
   */
  //Remove whitespace from the last line in the expected output.
  @Test
  public void testStartGame1() {
    List deck = testModel.getDeck();
    assertEquals(52, testModel.getDeck().size());
    testModel.startGame(deck, false);
    String st = testModel.getGameState();

    assertEquals(st, "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C4: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C5: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "C8: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣");

  }

  @Test
  public void testStartGame2() {
    List deck = testModel.getDeck();
    assertEquals(52, testModel.getDeck().size());
    testModel.startGame(deck, true);
    assertNotEquals(testModel.getGameState(), initialGameState);
  }


  @Test
  public void testStartGameNoshuffle() {
    testModel.startGame(testModel.getDeck(), false);
    assertEquals(testModel.getGameState(), initialGameState);
  }


  @Test
  public void testStartGameShuffle() {
    testModel.startGame(testModel.getDeck(), true);
    assertNotEquals(testModel.getGameState(), initialGameState);
  }

  @Test
  public void testStartGameAfterMoves() {
    testModel.startGame(testModel.getDeck(), false);
    testModel.move(PileType.CASCADE, 0, 13, PileType.OPEN, 0);
    testModel.move(PileType.CASCADE, 1, 13, PileType.OPEN, 1);
    testModel.move(PileType.CASCADE, 2, 13, PileType.OPEN, 2);
    testModel.move(PileType.CASCADE, 3, 13, PileType.OPEN, 3);

    testModel.startGame(testModel.getDeck(), false);
    assertEquals(testModel.getGameState(), initialGameState);

    testModel.move(PileType.CASCADE, 0, 13, PileType.OPEN, 0);
    testModel.move(PileType.CASCADE, 1, 13, PileType.OPEN, 1);
    testModel.move(PileType.CASCADE, 2, 13, PileType.OPEN, 2);
    testModel.move(PileType.CASCADE, 3, 13, PileType.OPEN, 3);
    testModel.move(PileType.CASCADE, 0, 11, PileType.CASCADE, 6);
    testModel.move(PileType.CASCADE, 1, 11, PileType.CASCADE, 7);
    testModel.move(PileType.CASCADE, 2, 11, PileType.CASCADE, 4);
    testModel.move(PileType.CASCADE, 3, 11, PileType.CASCADE, 5);

    testModel.startGame(testModel.getDeck(), false);
    assertEquals(testModel.getGameState(), initialGameState);
  }

  /*
  Initial game state is empty string.
   */
  @Test
  public void testGamestateInitial() {
    assertEquals(testModel.getGameState(), "");
  }

  @Test(expected = IllegalStateException.class)
  public void testMovebeforeGameStart() {
    testModel.getDeck();
    testModel.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 3);
    fail();
  }

  @Test
  public void testGameStateAfterStartNoShuffle() {
    testModel.startGame(testModel.getDeck(), false);
    assertEquals(testModel.getGameState(), initialGameState);
  }

  @Test
  public void testGameStateAfterStartShuffle() {
    testModel.startGame(testModel.getDeck(), true);
    assertNotEquals(testModel.getGameState(), initialGameState);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove() {
    testModel3.startGame(testModel3.getDeck(), false);
    //Valid moves
    //cascade to open

    testModel3.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    //cascade to foundation
    testModel3.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    //cascade to cascade
    testModel3.move(PileType.CASCADE, 2, 0, PileType.CASCADE, 5);

    //Invalid moves

    //cascade to open--pile is not empty
    testModel3.move(PileType.CASCADE, 6, 0, PileType.OPEN, 0);
    fail();
    //cascade to foundation-- invalid move works both on empty and with card
    testModel3.move(PileType.CASCADE, 6, 0, PileType.FOUNDATION, 1);
    fail();
    //cascade to cascade
    testModel3.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 4);
    fail();


    //Foundation to open
    testModel3.move(PileType.FOUNDATION, 1, 0, PileType.OPEN, 2);
    //Foundation to cascade
    testModel3.move(PileType.FOUNDATION, 1, 0, PileType.CASCADE, 0);
    fail();
  }


  @Test
  public void testisGameOverNo() {
    testModel.startGame(testModel.getDeck(), false);
    assertEquals(testModel.isGameOver(), false);
  }


  @Test
  public void testisGameOverYes() {

    //System.out.println(testModel3.getDeck().toString());
    //System.out.println(testModel3.isGameOver());
    //System.out.println(testModel3.getGameState());
    testModel3.startGame(testModel3.getDeck(), false);


    testModel3.move(PileType.CASCADE, 0, 1, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 1, 1, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 2, 1, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 3, 1, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 4, 2, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 5, 2, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 6, 2, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 7, 2, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 8, 3, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 9, 3, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 10, 3, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 11, 3, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 12, 4, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 13, 4, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 14, 4, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 15, 4, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 16, 5, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 17, 5, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 18, 5, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 19, 5, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 20, 6, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 21, 6, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 22, 6, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 23, 6, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 24, 7, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 25, 7, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 26, 7, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 27, 7, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 28, 8, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 29, 8, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 30, 8, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 31, 8, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 32, 9, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 33, 9, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 34, 9, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 35, 9, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 36, 10, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 37, 10, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 38, 10, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 39, 10, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 40, 11, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 41, 11, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 42, 11, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 43, 11, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 44, 12, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 45, 12, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 46, 12, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 47, 12, PileType.FOUNDATION, 3);
    testModel3.move(PileType.CASCADE, 48, 13, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 49, 13, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 50, 13, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 51, 13, PileType.FOUNDATION, 3);


    assertEquals(testModel3.isGameOver(), true);

  }


}