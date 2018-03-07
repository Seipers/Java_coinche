public class Bet
{
    CardEnum color;
    int bet;
    boolean coinche;
    boolean surcoinche;
    boolean pass;

    public Bet()
    {
        this.coinche = false;
        this.surcoinche = false;
        this.pass = false;
        this.bet = 0;
    }

    public CardEnum getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = CardEnum.values()[color - 1];
    }

    public int getBet()
    {
        return bet;
    }

    public void setBet(int bet)
    {
        this.bet = bet;
    }
}
