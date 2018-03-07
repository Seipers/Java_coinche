public enum CardNbEnum
{
    _7(7, "Seven", "7", 0, 0),
    _8(8, "Eight", "8",0, 0),
    _9(9, "Nine", "9",14, 0),
    _10(10, "Ten", "10", 10, 10),
    J(11, "Jack", "J", 20, 2),
    Q(12, "Queen", "Q", 3, 3),
    K(13, "King", "K", 4, 4),
    A(1, "As", "A", 11, 11);

    private int type;
    private String name;
    private String code;
    private int     trump;
    private int     notrump;

    CardNbEnum(int type, String name, String code, int _trump, int _notrump)
    {
        this.type = type;
        this.code = code;
        this.name = name;
        this.trump = _trump;
        this.notrump = _notrump;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    public int getTrump() {
        return trump;
    }

    public int getNotrump() {
        return notrump;
    }
}