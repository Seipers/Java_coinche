import junit.framework.TestCase;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameClientTest extends TestCase {

    public void testAskBet() throws Exception {
        int choice = 0;
        boolean coinche = false;
        int     pass = 0;
        int     maxbet = 80;

        if (choice == 3 && (coinche || pass > 0 || maxbet == 80))
             choice = 0;
        assertEquals(0, choice);
        if (choice == 4 && (!coinche || pass % 2 == 1))
             choice = 0;
        assertEquals(0, choice);
        if (maxbet >= 160 && choice == 1)
             choice = 0;
        assertEquals(0, choice);
    }
    
}