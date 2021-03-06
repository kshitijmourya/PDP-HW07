import org.junit.Before;
import org.junit.Test;

import java.util.List;

import freecell.model.Cards;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;


public class FreecellMultiMoveModelTest {
  private FreecellOperations testModel;
  private FreecellOperations testModelMultiMove;
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
    testModel = FreecellMultiMoveModel
            .getBuilder()
            .build();

    testModelMultiMove = FreecellMultiMoveModel
            .getBuilder()
            .opens(8)
            .build();

    testModel2 = FreecellMultiMoveModel
            .getBuilder()
            .cascades(4)
            .opens(2)
            .build();

    testModel3 = FreecellMultiMoveModel
            .getBuilder()
            .cascades(52)
            .opens(4)
            .build();

    FreecellOperations testModel4 = FreecellMultiMoveModel
            .getBuilder()
            .cascades(10)
            .opens(1)
            .build();

    FreecellOperations testModel5 = FreecellMultiMoveModel
            .getBuilder()
            .cascades(6)
            .build();
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
  public void testGameStateafterException() {
    testModel.startGame(testModel.getDeck(), false);
    testModel.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 2);
    System.out.println(testModel.getGameState());
    assertEquals(testModel.getGameState(), "F1:\n" +
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
  public void testStartGameAfterMoves() {
    testModelMultiMove.startGame(testModelMultiMove.getDeck(), false);
    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    System.out.println(testModelMultiMove.getGameState());


    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 1);
    //testModelMultiMove.move(PileType.CASCADE, 6, 12, PileType.CASCADE, 0);
    //testModelMultiMove.move(PileType.CASCADE, 7, 12, PileType.CASCADE, 1);

    //testModelMultiMove.startGame(testModelMultiMove.getDeck(), false);
    //assertEquals(testModelMultiMove.getGameState(), initialGameState);

    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 2);
    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 3);
    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 4);
    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.OPEN, 5);
    System.out.println(testModelMultiMove.getGameState());

    testModelMultiMove.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    System.out.println(testModelMultiMove.getGameState());

    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    System.out.println(testModelMultiMove.getGameState());

    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 6, 0, PileType.CASCADE, 0);
    testModelMultiMove.move(PileType.CASCADE, 1, 0, PileType.CASCADE, 0);
    System.out.println(testModelMultiMove.getGameState());


    //System.out.println(testModelMultiMove.getGameState());

    testModelMultiMove.move(PileType.OPEN, 0, 0, PileType.CASCADE, 1);
    testModelMultiMove.move(PileType.CASCADE, 7, 0, PileType.CASCADE, 1);
    testModelMultiMove.move(PileType.OPEN, 1, 0, PileType.CASCADE, 1);
    testModelMultiMove.move(PileType.CASCADE, 7, 0, PileType.CASCADE, 1);
    testModelMultiMove.move(PileType.OPEN, 2, 0, PileType.CASCADE, 1);
    testModelMultiMove.move(PileType.CASCADE, 7, 0, PileType.CASCADE, 1);

    testModelMultiMove.move(PileType.OPEN, 3, 0, PileType.CASCADE, 1);
    System.out.println(testModelMultiMove.getGameState());

    testModelMultiMove.move(PileType.CASCADE, 0, 5, PileType.CASCADE, 1);
    System.out.println(testModelMultiMove.getGameState());
    testModelMultiMove.move(PileType.CASCADE, 7, 0, PileType.OPEN, 7);
    System.out.println(testModelMultiMove.getGameState());
    //    testModelMultiMove.move(PileType.CASCADE, 7, 0, PileType.OPEN, 5);
    System.out.println(testModelMultiMove.getGameState());

    /**

     testModelMultiMove.move(PileType.CASCADE, 7, 6, PileType.CASCADE, 6);
     testModelMultiMove.move(PileType.CASCADE, 7, 4, PileType.OPEN, 7);
     testModelMultiMove.move(PileType.CASCADE, 0, 6, PileType.CASCADE, 1
     );
     //System.out.println(testModelMultiMove.getGameState());

     testModelMultiMove.move(PileType.FOUNDATION, 0, 1, PileType.OPEN, 0);
     System.out.println(testModelMultiMove.getGameState());

     testModelMultiMove.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 0);

     // testModelMultiMove.move(PileType.CASCADE, 1, 1, PileType.FOUNDATION, 3);

     System.out.println(testModelMultiMove.getGameState());

     //testModel.startGame(testModel.getDeck(), false);
     assertNotEquals(testModel.getGameState(), initialGameState);
     */
    assertEquals(testModelMultiMove.isGameOver(), false);
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

    //System.out.print(testModel3.getGameState());
    testModel3.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 2, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 3, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 5, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 6, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 7, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 8, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 9, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 10, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 11, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 12, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 13, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 14, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 15, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 16, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 17, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 18, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 19, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 20, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 21, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 22, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 23, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 24, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 25, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 26, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 27, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 28, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 29, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 30, 0, PileType.FOUNDATION, 2);
    //System.out.println(testModel3.getGameState());
    testModel3.move(PileType.CASCADE, 31, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 32, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 33, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 34, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 35, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 36, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 37, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 38, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 39, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 40, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 41, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 42, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 43, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 44, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 45, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 46, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 47, 0, PileType.FOUNDATION, 3);

    testModel3.move(PileType.CASCADE, 48, 0, PileType.FOUNDATION, 0);
    testModel3.move(PileType.CASCADE, 49, 0, PileType.FOUNDATION, 1);
    testModel3.move(PileType.CASCADE, 50, 0, PileType.FOUNDATION, 2);
    testModel3.move(PileType.CASCADE, 51, 0, PileType.FOUNDATION, 3);

    System.out.println(testModel3.getGameState());


    assertEquals(testModel3.isGameOver(), true);


  }


  @Test
  public void testValidCascadeMoves() {
    testModel3.startGame(testModel3.getDeck(), false);
    System.out.println(testModel3.getGameState());

    //cascade to cascade single
    testModel3.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 7);


    //cascade to cascade multiple
    testModel3.move(PileType.CASCADE, 7, 1, PileType.CASCADE, 8);
    testModel3.move(PileType.CASCADE, 8, 2, PileType.CASCADE, 14);

    //cascade to open pile
    testModel3.move(PileType.CASCADE, 9, 0, PileType.OPEN, 0);


    //cascade to foundation pile
    testModel3.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 1);
    System.out.println(testModel3.getGameState());

    assertEquals(testModel3.getGameState(), "F1:\n" +
            "F2: A♦\n" +
            "F3:\n" +
            "F4:\n" +
            "O1: 3♦\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1:\n" +
            "C2:\n" +
            "C3: A♠\n" +
            "C4: A♣\n" +
            "C5: 2♥\n" +
            "C6: 2♦\n" +
            "C7: 2♠\n" +
            "C8:\n" +
            "C9:\n" +
            "C10:\n" +
            "C11: 3♠\n" +
            "C12: 3♣\n" +
            "C13: 4♥\n" +
            "C14: 4♦\n" +
            "C15: 4♠, 3♥, 2♣, A♥\n" +
            "C16: 4♣\n" +
            "C17: 5♥\n" +
            "C18: 5♦\n" +
            "C19: 5♠\n" +
            "C20: 5♣\n" +
            "C21: 6♥\n" +
            "C22: 6♦\n" +
            "C23: 6♠\n" +
            "C24: 6♣\n" +
            "C25: 7♥\n" +
            "C26: 7♦\n" +
            "C27: 7♠\n" +
            "C28: 7♣\n" +
            "C29: 8♥\n" +
            "C30: 8♦\n" +
            "C31: 8♠\n" +
            "C32: 8♣\n" +
            "C33: 9♥\n" +
            "C34: 9♦\n" +
            "C35: 9♠\n" +
            "C36: 9♣\n" +
            "C37: 10♥\n" +
            "C38: 10♦\n" +
            "C39: 10♠\n" +
            "C40: 10♣\n" +
            "C41: J♥\n" +
            "C42: J♦\n" +
            "C43: J♠\n" +
            "C44: J♣\n" +
            "C45: Q♥\n" +
            "C46: Q♦\n" +
            "C47: Q♠\n" +
            "C48: Q♣\n" +
            "C49: K♥\n" +
            "C50: K♦\n" +
            "C51: K♠\n" +
            "C52: K♣");
  }


  @Test
  public void testOpenValidMoves() {
    testModel3.startGame(testModel3.getDeck(), false);
    //System.out.println(testModel3.getGameState());
    //cascade to open

    testModel3.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    testModel3.move(PileType.CASCADE, 1, 0, PileType.OPEN, 1);
    //System.out.println(testModel3.getGameState());

    //open to foundation
    testModel3.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);

    //open to cascade
    testModel3.move(PileType.OPEN, 1, 0, PileType.CASCADE, 7);

    //System.out.println(testModel3.getGameState());

    assertEquals(testModel3.getGameState(), "F1: A♥\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1:\n" +
            "C2:\n" +
            "C3: A♠\n" +
            "C4: A♣\n" +
            "C5: 2♥\n" +
            "C6: 2♦\n" +
            "C7: 2♠\n" +
            "C8: 2♣, A♦\n" +
            "C9: 3♥\n" +
            "C10: 3♦\n" +
            "C11: 3♠\n" +
            "C12: 3♣\n" +
            "C13: 4♥\n" +
            "C14: 4♦\n" +
            "C15: 4♠\n" +
            "C16: 4♣\n" +
            "C17: 5♥\n" +
            "C18: 5♦\n" +
            "C19: 5♠\n" +
            "C20: 5♣\n" +
            "C21: 6♥\n" +
            "C22: 6♦\n" +
            "C23: 6♠\n" +
            "C24: 6♣\n" +
            "C25: 7♥\n" +
            "C26: 7♦\n" +
            "C27: 7♠\n" +
            "C28: 7♣\n" +
            "C29: 8♥\n" +
            "C30: 8♦\n" +
            "C31: 8♠\n" +
            "C32: 8♣\n" +
            "C33: 9♥\n" +
            "C34: 9♦\n" +
            "C35: 9♠\n" +
            "C36: 9♣\n" +
            "C37: 10♥\n" +
            "C38: 10♦\n" +
            "C39: 10♠\n" +
            "C40: 10♣\n" +
            "C41: J♥\n" +
            "C42: J♦\n" +
            "C43: J♠\n" +
            "C44: J♣\n" +
            "C45: Q♥\n" +
            "C46: Q♦\n" +
            "C47: Q♠\n" +
            "C48: Q♣\n" +
            "C49: K♥\n" +
            "C50: K♦\n" +
            "C51: K♠\n" +
            "C52: K♣");
  }

  @Test
  public void testInvalid1() {
    testModel3.startGame(testModel3.getDeck(), false);
    System.out.println(testModel3.getGameState());

    testModel3.move(PileType.CASCADE, 5, 0, PileType.OPEN, 2);
    System.out.println(testModel3.getGameState());
    testModel3.move(PileType.CASCADE, 7, 0, PileType.OPEN, 2);
    System.out.println(testModel3.getGameState());
    assertEquals(testModel3.isGameOver(), false);

  }
  /**
   * Tests move method
   * 1) move single cascade to cascade
   * 2) move single cascade to open
   * 3) move single cascade to foundation
   * 4) move multiple cascade to cascade
   * 5) invalid cascade source build
   * 6) invalid cascade destination build
   * 7) open pile not free
   * 8) foundation pile incorrect build
   * 9) move to non empty cascade
   * 10) move to empty cascade
   * 11) move multiple to open fail
   *
   * IsGameOver
   * 1) yes
   * 2) no
   *
   *
   * Gamestate
   * 1) before start game
   * 2) after start game
   * 3) after any exception
   * 4) after moves
   *
   *
   * Tests
   * 1) invalid deck
   * 2) cascadepiles<4 open pile<1
   * 3)move before gamestart
   * 4) test shuffle
   * 5)
   */


}