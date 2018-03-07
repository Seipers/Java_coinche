import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Team
 */

public class Team
{
    private ClientSide P1;
    private ClientSide P2;
    private int score = 0;
    private int scoreDraw = 0;
    private Bet bet = new Bet();

    /**
     * @return the score
     */
    public int getScore()
    {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScoreDraw(int scoreDraw) {
        this.scoreDraw = scoreDraw;
    }

    public void notifyScore(int point, String team){
        ServerRequest req = new ServerRequest();

        req.setOpcode(OpcodeEnum.WINDRAW);
        req.setPoints(point);
        req.setTeam(team);
        P1.getSocket().sendTCP(req);
        P2.getSocket().sendTCP(req);
    }

    public void disconnect()
    {
        if (P1 != null)
            P1.getSocket().close();
        if (P2 != null)
        P2.getSocket().close();
    }

    /**
     * @return the scoreDraw

     */
    public int getScoreDraw()
    {
        return scoreDraw;
    }

    public boolean addPlayer(ClientSide player)
    {
        if (this.P1 == null)
            this.P1 = player;
        else if (this.P2 == null)
            this.P2 = player;
        else
            return false;
        return true;
    }

    public int size()
    {
        int i = 0;

        if (this.P1 != null)
            i++;
        if (this.P2 != null)
            i++;
        return (i);
    }

    public boolean isFull()
    {
        if (this.P1 != null && this.P2 != null)
            return true;
        return false;
    }

    /**
     * @param bet the bet to set
     */
    public void setBet(Bet bet)
    {
        this.bet = bet;
    }

    /**
     * @return the bet
     */
    public Bet getBet()
    {
        return bet;
    }

    public void AskBet(int player)
    {
        Connection socket;
        ServerRequest req = new ServerRequest();
        
        if (player == 0)
            socket = this.P1.getSocket();
        else
            socket = this.P2.getSocket();
        req.setOpcode(OpcodeEnum.ASKBET);
        socket.sendTCP(req);
    }

    public void sendCard(int player, List<Card> deck)
    {
        Connection socket;
        ServerRequest req = new ServerRequest();

        if (player == 0)
        {
            P1.setCards(deck);
            socket = P1.getSocket();
        }
        else
        {
            P2.setCards(deck);
            socket = P2.getSocket();
        }
        req.setOpcode(OpcodeEnum.DEAL);
        req.setCards(deck);
        socket.sendTCP(req);
    }

    public void setMyBet(int i, ClientRequest resp)
    {
        if (!resp.getMybet().pass && !resp.getMybet().surcoinche)
            this.bet = resp.getMybet();
        ServerRequest event = new ServerRequest();
        Connection socket;
        event.setOpcode(OpcodeEnum.OTHERBET);
        event.setMybet(resp.getMybet());
        event.setTeam("Ally");
        if (i != 0)
            socket = P1.getSocket();
        else
            socket = P2.getSocket();
        socket.sendTCP(event);
    }

    public void setEnemyBet(ClientRequest resp)
    {
        ServerRequest event = new ServerRequest();
        Connection socket;
        event.setOpcode(OpcodeEnum.OTHERBET);
        event.setMybet(resp.getMybet());
        event.setTeam("Enemy");
        socket = P1.getSocket();
        socket.sendTCP(event);
        socket = P2.getSocket();
        socket.sendTCP(event);
	}

	public void sendWinnerRound(Bet bet, String str){
        ServerRequest req = new ServerRequest();

        req.setOpcode(OpcodeEnum.DEALER);
        req.setTeam(str);
        req.setMybet(bet);
        P1.getSocket().sendTCP(req);
        P2.getSocket().sendTCP(req);
    }

    public void play_card(int player){
	   ServerRequest    req = new ServerRequest();

	   req.setOpcode(OpcodeEnum.ASKPLAY);
        if (player == 0){
            P1.getSocket().sendTCP(req);
        }else{
            P2.getSocket().sendTCP(req);
        }
    }

    public boolean play(int index, Card card_played, ArrayList<Card> hand){
        boolean ret = false;
        ServerRequest req = new ServerRequest();

        req.setOpcode(OpcodeEnum.OTHERPLAY);
        req.setTeam("Ally ");
        req.setCard(card_played);
        if (index == 0) {
            if (ret = P1.play(card_played, hand))
                P2.getSocket().sendTCP(req);
        }
        else {
            if (ret = P2.play(card_played, hand))
                P1.getSocket().sendTCP(req);
        }
        return (ret);
    }

    public void EnemyPlay(Card mycard) {
        ServerRequest req = new ServerRequest();

        req.setOpcode(OpcodeEnum.OTHERPLAY);
        req.setTeam("Enemy ");
        req.setCard(mycard);
        P1.getSocket().sendTCP(req);
        P2.getSocket().sendTCP(req);
    }

    public void sendScore(int sc) {
        ServerRequest req = new ServerRequest();

        req.setEnnemy_points(sc);
        req.setPoints(getScore());
        req.setOpcode(OpcodeEnum.SCORE);
        P1.getSocket().sendTCP(req);
        P2.getSocket().sendTCP(req);
    }
}