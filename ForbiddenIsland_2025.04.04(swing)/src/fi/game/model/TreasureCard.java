package fi.game.model;

/**
 * 宝藏卡类
 */
public class TreasureCard {
    private CardType type;
    private String name;
    private int id;
    private static int nextId = 0;

    public TreasureCard(CardType type, String name) {
        this.type = type;
        this.name = name;
        this.id = nextId++;
    }

    public CardType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TreasureCard) {
            return ((TreasureCard) obj).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
