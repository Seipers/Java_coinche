import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientSide {

    String              name = "";
    Connection          socket;
    List<Card>          cards;

    public ClientSide(Connection socket) {
        this.socket = socket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Connection socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public Connection getSocket() {
        return socket;
    }

    public void setCards(List<Card> cards)
    {
        this.cards = cards;
    }


    private boolean verify_card(Card card, ArrayList<Card> hand)
    {
        if (hand.size() > 0) {
            if (card.getColor() != hand.get(0).getColor()) {
                for (Card tmp : cards) {
                    if (tmp.getColor() == hand.get(0).getColor())
                        return false;
                }
            }
        }
        return true;
    }

    private void resend_card(Card card_played){
        ServerRequest   req = new ServerRequest();

        req.setOpcode(OpcodeEnum.BADCARD);
        req.setCard(card_played);
        socket.sendTCP(req);
    }

    public boolean play(Card card_played, ArrayList<Card> hand) {

        if (!verify_card(card_played, hand))
        {
            resend_card(card_played);
            return false;
        }
        for (Iterator<Card> iter = cards.listIterator(); iter.hasNext(); ) {
            Card tmp = iter.next();
            if (tmp.getColor() == card_played.getColor() && tmp.getNumber() == card_played.getNumber()) {
                System.out.println("[*] " + name + " Played " + card_played.getColor().getName() + " " + card_played.getNumber().getName());
                return (true);
            }
        }
        resend_card(card_played);
        return false;
    }
}
