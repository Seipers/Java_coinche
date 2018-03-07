import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Board
 */

public class Board {

    Team            team1 = new Team();
    Team            team2 = new Team();
    ArrayList<Card> deck = new ArrayList<Card>();
    ArrayList<Card> hand = new ArrayList<Card>();
    private int     turn;
    boolean         coinche;
    boolean         surcoinche;
    int             pass;
    private int     round_winner = -1;
    Bet             max_bet = new Bet();


    public Board()
    {
        this.generateDeck();
        new Thread("Play")
        {
            public void run()
            {
                Game();
            }
        }.start();
    }

    private void generateDeck()
    {
        for (int i = 1; i <= 4; i++)
        {
            for (int j = 7; j <= 14; j++)
            {
                Card card = new Card(i, j);
                deck.add(card);
            }
        }
    }

    public synchronized void interrupt_game(){
        team1.disconnect();
        team2.disconnect();
        Thread.currentThread().interrupt();
    }

    public synchronized boolean addPlayer(ClientSide player)
    {
        if (team1.isFull() && team2.isFull())
            return false;
        if (team1.size() <= team2.size())
        {
            team1.addPlayer(player);
            System.out.println("[+] " + player.getName() + " join team 1");
        }
        else
        {
            team2.addPlayer(player);
            System.out.println("[+] " + player.getName() + " join team 2");
        }
        if (team1.isFull() && team2.isFull())
            notify();
        return true;
    }

    public synchronized void Game()
    {
        System.out.println("[+] Waiting 4 players");
        try { wait(); }
        catch (InterruptedException e)
        {
			e.printStackTrace();
        }
        while (team1.getScore() < 701 && team2.getScore() < 701) {
            this.sendCard();
            System.out.println("[+] Send Cards");
            this.Bet();
            winner_notify();
            calc_turn();
            play_card();
            calcWinBet();
        }
        System.out.println("[*] End of game");
        team1.disconnect();
        team2.disconnect();
    }

    private void calcWinBet()
    {
        int bet = max_bet.getBet();
        if (bet == team1.getBet().getBet())
        {
            if (team1.getScoreDraw() < bet)
                team2.setScore(team2.getScore() + 162 + bet);
            else
                team1.setScore(team1.getScore() + team1.getScoreDraw() + bet);
        }
        else
        {
            if (team2.getScoreDraw() < bet)
                team1.setScore(team1.getScore() + 162 + bet);
            else
                team2.setScore(team2.getScore() + team2.getScoreDraw() + bet);
        }
        coinche = false;
        surcoinche = false;
        pass    = 0;
        max_bet = new Bet();
        team1.setScoreDraw(0);
        team2.setScoreDraw(0);
        team1.sendScore(team2.getScore());
        team2.sendScore(team1.getScore());
    }

    public void winner_notify(){
        Bet     tmp;
        String  str;
        String  str2;

        if (round_winner % 2 == 0){
            tmp = team1.getBet();
            max_bet = team1.getBet();
            str = "Ally";
            str2 = "Enemy";
        }else{
            tmp = team2.getBet();
            max_bet = team2.getBet();
            str = "Enemy";
            str2 = "Ally";
        }
        tmp.coinche = this.coinche;
        tmp.surcoinche = this.surcoinche;
        team1.sendWinnerRound(tmp, str);
        team2.sendWinnerRound(tmp, str2);
    }

    private void sendCard()
    {
        Collections.shuffle(deck);
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        List<Card> deck3 = new ArrayList<>();
        List<Card> deck4 = new ArrayList<>();
        
        int i = 0;
        Iterator<Card> c = deck.iterator();
        while (i <= 7)
        {
            deck1.add(c.next());
            c.remove();
            deck2.add(c.next());
            c.remove();
            deck3.add(c.next());
            c.remove();
            deck4.add(c.next());
            c.remove();
            i++;
        }
        team1.sendCard(0, deck1);
        team1.sendCard(1, deck2);
        team2.sendCard(0, deck3);
        team2.sendCard(1, deck4);
    }

    private boolean endBet()
    {
        if (surcoinche || (pass == 3 && max_bet.getBet() > 70))
            return true;
        if (pass == 3)
        {
            this.generateDeck();
            this.sendCard();
        }
        return false;
    }

    private synchronized void Bet()
    {
        this.coinche = false;
        this.surcoinche = false;
        this.pass = 0;
        this.turn = (int)(Math.random() * 4);

        while (!this.endBet())
        {
            System.out.println("[+] Player " + this.turn + " turn");
            if (turn % 2 == 0)
                team1.AskBet(turn / 2);
            else
                team2.AskBet(turn / 2);
            try { wait(); } 
            catch (InterruptedException e) {
    			e.printStackTrace();
            }
            this.turn = (this.turn + 1) % 4;
        }
    }

    public synchronized void setBet(ClientRequest resp)
    {
        if (!verifyBet(resp.getMybet()))
            return;
        if (turn % 2 == 0)
        {
            team1.setMyBet(turn / 2, resp);
            team2.setEnemyBet(resp);
        }
        else
        {
            team2.setMyBet(turn / 2, resp);
            team1.setEnemyBet(resp);
        }
        Bet bet = resp.getMybet();
        this.coinche = bet.coinche;
        this.surcoinche = bet.surcoinche;
        if (bet.pass == true)
            this.pass++;
        else if (this.pass != 0)
            this.pass = 0;
        calc_winner_round(bet);
        notify();
    }

    private boolean verifyBet(Bet mybet) {
        Bet enemy;
        boolean ok = true;

        if (turn % 2 == 0)
            enemy = team2.getBet();
        else
            enemy = team1.getBet();
        if (mybet.getBet() < max_bet.getBet() && (!mybet.pass && !mybet.coinche && !mybet.surcoinche))
            ok = false;
        if (mybet.surcoinche && !enemy.coinche)
            ok = false;
        if (!ok)
        {
            if (turn % 2 == 0)
                team1.AskBet(turn / 2);
            else
                team2.AskBet(turn / 2);
        }
        return (ok);
    }

	public void calc_winner_round(Bet bet){
        if (!bet.coinche && !bet.surcoinche && !bet.pass){
            max_bet.setBet(bet.getBet());
            this.round_winner = this.turn;
        }
    }

    public void calc_turn(){
        if (round_winner == 0)
            turn = 3;
        else
            turn = round_winner - 1;
    }

    public void setPointToWinner(int point){

        String  str;
        String  str2;

        if (turn % 2 == 0){
            team1.setScoreDraw(team1.getScoreDraw() + point);
            str = "Ally";
            str2 = "Enemy";
        }else{
            team2.setScoreDraw(team2.getScoreDraw() + point);
            str2 = "Ally";
            str = "Enemy";
        }
        team1.notifyScore(point, str);
        team2.notifyScore(point, str2);
        hand.clear();
    }

    public void managePoint(){
        int     hand_value = 0;
        int     index = 0;
        int     winner = 0;

        /* Loop to calc hand value */
        for (Card tmp:hand) {
            if (tmp.getColor() == max_bet.getColor()){
                hand_value += tmp.getNumber().getTrump();
                if (hand.get(winner).getColor() == tmp.getColor()){
                    if (hand.get(winner).getNumber().getTrump() < tmp.getNumber().getTrump())
                        winner = index;
                    else if (hand.get(winner).getNumber().getTrump() == tmp.getNumber().getTrump())
                    {
                        if (hand.get(winner).getNumber().getType() < tmp.getNumber().getType())
                            winner = index;
                    }
                }
            }else{
                hand_value += tmp.getNumber().getNotrump();
                if (hand.get(winner).getNumber().getNotrump() < tmp.getNumber().getNotrump())
                    winner = index;
                else if (hand.get(winner).getNumber().getNotrump() == tmp.getNumber().getNotrump())
                {
                    if (hand.get(winner).getNumber().getType() < tmp.getNumber().getType())
                        winner = index;
                }
            }
            index++;
        }
        System.out.println("[+] Hand value: " + hand_value);
        turn = (turn + winner) % 4;
        setPointToWinner(hand_value);
    }

    public synchronized void play_card(){
        while (deck.size() < 32){
            if (turn % 2 == 0){
                team1.play_card(turn / 2);
            }else{
                team2.play_card(turn / 2);
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.turn = (this.turn + 1) % 4;
            if (hand.size() == 4){
                managePoint();
            }
        }
        System.out.println("[*] Round over");
    }

    public synchronized void client_play(ClientRequest req){
        boolean resp;

        if (turn % 2 == 0) {
            if ((resp = team1.play(turn / 2, req.getMycard(), hand)))
                team2.EnemyPlay(req.getMycard());
        }
        else {
              if ((resp = team2.play(turn / 2, req.getMycard(), hand)))
                team1.EnemyPlay(req.getMycard());
        }
        if (resp){
            deck.add(req.getMycard());
            hand.add(req.getMycard());
        }
        //Notify client (error);
        notify();
    }

}