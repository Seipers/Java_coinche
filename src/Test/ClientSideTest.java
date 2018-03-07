import junit.framework.TestCase;

import java.util.ArrayList;

public class ClientSideTest extends TestCase {

    private boolean verify_card(ArrayList<Card> hand, Card card)
    {
        boolean state = false;

        if (hand.size() > 0) {
            if (card.getColor() != hand.get(0).getColor())
                return false;
        }
        return true;
    }

    public void testPlay() throws Exception {
        ArrayList<Card> hand = new ArrayList<Card>();
        Card tmp = new Card();

        assertTrue(verify_card(hand, tmp));

        tmp.setColor(CardEnum.CLOVER);
        tmp.setNumber(CardNbEnum.A);
        hand.add(tmp);
        tmp = new Card();
        tmp.setNumber(CardNbEnum.A);
        tmp.setColor(CardEnum.HEART);

        assertFalse(verify_card(hand, tmp));
    }


}