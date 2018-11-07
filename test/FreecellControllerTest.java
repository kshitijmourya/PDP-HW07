import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.MockModel;
import freecell.controller.IFreecellController;
import freecell.controller.FreecellController;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;


public class FreecellControllerTest {
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
   * setup method.
   */
  @Before
  public void setUp() {
    FreecellOperations testModel2 = FreecellModel
            .getBuilder()
            .cascades(8)
            .opens(4)
            .build();
  }

  @Test
  public void test1() {

    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("F7 2 C6 s\n q");
    IFreecellController controller = new FreecellController(in, out);
    StringBuilder log = new StringBuilder();
    controller.playGame(new ArrayList<>(), new MockModel(log, 628), true);


    assertEquals("Input: FOUNDATION 6 1 CASCADE 5\n", log.toString());
  }


}


