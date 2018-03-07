import java.util.List;

public class ServerRequest {
    OpcodeEnum  opcode;
    int         nb_player;
    List<Card>  cards;
    Card        card;
    String      team;
    String      name;
    Bet         mybet;
    int         points;
    int         Ennemy_points;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ServerRequest() {
    }

    public void setOpcode(OpcodeEnum opcode) {
        this.opcode = opcode;
    }

    public OpcodeEnum getOpcode() {
        return opcode;
    }

    public int getNb_player() {
        return nb_player;
    }

    public void setNb_player(int nb_player) {
        this.nb_player = nb_player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bet getMybet() {
        return mybet;
    }

    public void setMybet(Bet mybet) {
        this.mybet = mybet;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getEnnemy_points() {
        return Ennemy_points;
    }

    public void setEnnemy_points(int ennemy_points) {
        Ennemy_points = ennemy_points;
    }
}
