public class ClientRequest {
    OpcodeEnum     opcode;
    String  name;
    Bet     mybet;
    Card    mycard;

    public ClientRequest() {}

    public void setOpcode(OpcodeEnum opcode) {
        this.opcode = opcode;
    }

    public OpcodeEnum getOpcode() {
        return opcode;
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

    public Card getMycard() {
        return mycard;
    }

    public void setMycard(Card mycard) {
        this.mycard = mycard;
    }
}
