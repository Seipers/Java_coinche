public class Card {

    CardEnum color;
    CardNbEnum number;

    public Card()
    {
    }

    public Card(int color, int number) {
        this.color = CardEnum.values()[color - 1];
        this.number = CardNbEnum.values()[number - 7];
    }

    public CardEnum getColor() {
        return color;
    }

    public void setColor(CardEnum color) {
        this.color = color;
    }

    public CardNbEnum getNumber() {
        return number;
    }

    public void setNumber(CardNbEnum number) {
        this.number = number;
    }
}
