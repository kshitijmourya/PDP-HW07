import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import freecell.controller.FreecellController;
import freecell.model.FreecellModel;
import freecell.model.FreecellModelAbstract;
import freecell.model.FreecellOperations;

public class FreecellControllerTest {
  private FreecellOperations testModel2;
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

  @Before
  public void setUp() {
    testModel2 = FreecellModel
            .getBuilder()
            .cascades(8)
            .opens(4)
            .build();
  }

  @Test
  public void test() {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("");

    FreecellController controller = new FreecellController(in, out);
    controller.playGame(testModel2.getDeck(),testModel2,false);
    System.out.println(testModel2.getDeck());
    assertEquals(initialGameState,out.toString());
  }
}