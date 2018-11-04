import java.util.List;

import freecell.model.FreecellModel;
import freecell.model.PileType;

public class TestMain {
  /**
   * main function.
   *
   * @param args args.
   */
  public static void main(String[] args) {
    helper(FreecellModel
            .getBuilder()
            .cascades(52)
            .opens(4)
            .build());


  }

  private static <T> void helper(freecell.model.FreecellOperations<T> model) {
    List<T> deck = model.getDeck();
    model.startGame(deck, false);
    //System.out.print(model.getGameState());
    //System.out.println();
    //System.out.println("**********************************************************");
    //System.out.println("**********************************************************");

    // Moves to finish the game.
    /* Cascade 52 open 4
    model.move(PileType.CASCADE,0,1,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,1,1,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,2,1,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,3,1,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,4,2,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,5,2,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,6,2,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,7,2,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,8,3,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,9,3,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,10,3,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,11,3,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,12,4,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,13,4,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,14,4,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,15,4,PileType.FOUNDATION,3);

    model.move(PileType.CASCADE,16,5,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,17,5,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,18,5,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,19,5,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,20,6,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,21,6,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,22,6,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,23,6,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,24,7,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,25,7,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,26,7,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,27,7,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,28,8,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,29,8,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,30,8,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,31,8,PileType.FOUNDATION,3);

    model.move(PileType.CASCADE,32,9,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,33,9,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,34,9,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,35,9,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,36,10,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,37,10,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,38,10,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,39,10,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,40,11,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,41,11,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,42,11,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,43,11,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,44,12,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,45,12,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,46,12,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,47,12,PileType.FOUNDATION,3);
    model.move(PileType.CASCADE,48,13,PileType.FOUNDATION,0);
    model.move(PileType.CASCADE,49,13,PileType.FOUNDATION,1);
    model.move(PileType.CASCADE,50,13,PileType.FOUNDATION,2);
    model.move(PileType.CASCADE,51,13,PileType.FOUNDATION,3);
*/


    //System.out.println(model.getGameState());

   /* model.move(PileType.CASCADE,0,13,PileType.OPEN,0);
    model.move(PileType.CASCADE,1,13,PileType.OPEN,1);
    model.move(PileType.CASCADE,2,13,PileType.OPEN,2);
    model.move(PileType.CASCADE,3,13,PileType.OPEN,3);
    System.out.println(model.getGameState());
    System.out.println("##########################");
    model.startGame(deck,false);
    System.out.println(model.getGameState());
    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&");
    model.startGame(deck,true);
    System.out.println(model.getGameState());
    System.out.println("*************************");
    */

    //model.move(PileType.CASCADE, 0, 13, PileType.OPEN, 0);
    //model.move(PileType.CASCADE,1,13,PileType.OPEN,1);
    //model.move(PileType.CASCADE,2,13,PileType.OPEN,2);
    //model.move(PileType.CASCADE,3,13,PileType.OPEN,3);

    //model.startGame(deck,true);


    //Valid moves
    //cascade to open
    //model.move(PileType.CASCADE,0,0,PileType.OPEN,0);
    //cascade to foundation
    //model.move(PileType.CASCADE,1,0,PileType.FOUNDATION,1);
    //cascade to cascade
    //model.move(PileType.CASCADE,2,0,PileType.CASCADE,5);

    //Invalid moves

    //cascade to open--pile is not empty
   // model.move(PileType.CASCADE,6,0,PileType.OPEN,0);
    //cascade to foundation-- invalid move works both on empty and with card
    //model.move(PileType.CASCADE,6,0,PileType.FOUNDATION,1);
    //cascade to cascade
   // model.move(PileType.CASCADE,0,0,PileType.CASCADE,4);


    //Foundation to open
    //model.move(PileType.FOUNDATION,1,0,PileType.OPEN,2);
    //Foundation to cascade
    //model.move(PileType.FOUNDATION,1,0,PileType.CASCADE,0);


    //Open to Open

    //model.move(PileType.CASCADE,0,0,PileType.CASCADE,0);
    System.out.println(model.getGameState());

    System.out.println(model.isGameOver());
  }
}
