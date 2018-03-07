public enum CardEnum
{
    TILES(1, "Diamond", "D"),
    HEART(2, "Heart", "H"),
    CLOVER(3, "Club", "C"),
    SPADE(4, "Spade", "S");

    private int type;
    private String name;
    private String code;

    CardEnum(int type, String name, String code)
    {
        this.type = type;
        this.name = name;
        this.code = code;
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
}