import junit.framework.TestCase;

public class BoardTest extends TestCase {
    public void testInterrupt_game() throws Exception {
        Team            team1 = new Team();
        Team            team2 = new Team();

        team1.disconnect();
        team2.disconnect();
        assertEquals(0, team1.size());
        assertEquals(0, team2.size());
    }


    public void testCalc_winner_round() throws Exception {
        Bet bet = new Bet();
        Bet max_bet = new Bet();

        bet.setBet(160);
        if (!bet.coinche && !bet.surcoinche && !bet.pass){
            max_bet.setBet(bet.getBet());
        }
        assertEquals(bet.getBet(), max_bet.getBet());
    }

    public void testCalc_turn() throws Exception {
        int     turn = 0;
        int     round_winner = 0;

        if (round_winner == 0)
            turn = 3;
        else
            turn = round_winner - 1;
        assertEquals(3, turn);
        round_winner = 2;
        if (round_winner == 0)
            turn = 3;
        else
            turn = round_winner - 1;
        assertEquals(1, turn);

    }

    public void testSetPointToWinner() throws Exception {
        Team    team1 = new Team();
        Team    team2 = new Team();
        int     turn = 0;

        if (turn % 2 == 0) {
            team1.setScoreDraw(100);
        }else{
            team2.setScoreDraw(100);
        }
        assertEquals(team1.getScoreDraw(), 100);
        turn = 1;
        if (turn % 2 == 0) {
            team1.setScoreDraw(100);
        }else{
            team2.setScoreDraw(100);
        }
        assertEquals(team2.getScoreDraw(), 100);
    }

    public void testManagePoint() throws Exception {
    }

}