import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import freecell.controller.FreecellController;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import freecell.controller.FreecellController;
import freecell.model.FreecellModel;
import freecell.model.FreecellModelAbstract;
import freecell.model.FreecellOperations;



public class TestMain2 {
  public static void main(String[] args) {
    List<String> deck = new ArrayList<String>();
    helper(deck,
            FreecellModel
            .getBuilder()
            .cascades(8)
            .opens(4)
            .build(),
            false);

  }

  private static <T> void helper(List Deck, freecell.model.FreecellOperations<T> model,boolean shuffle) {

  }
}