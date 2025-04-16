package fi.game.model;


/**
 * 瓦片类型枚举
 */
public enum TileType {
    TEMPLE_OF_THE_MOON("Temple of the Moon", Treasure.THE_EARTH_STONE),
    TEMPLE_OF_THE_SUN("Temple of the Sun", Treasure.THE_EARTH_STONE),
    WHISPERING_GARDEN("Whispering Garden", Treasure.THE_STATUE_OF_THE_WIND),
    HOWLING_GARDEN("Howling Garden", Treasure.THE_STATUE_OF_THE_WIND),
    CAVE_OF_EMBERS("Cave of Embers", Treasure.THE_CRYSTAL_OF_FIRE),
    CAVE_OF_SHADOWS("Cave of Shadows", Treasure.THE_CRYSTAL_OF_FIRE),
    CORAL_PALACE("Coral Palace", Treasure.THE_OCEANS_CHALICE),
    TIDAL_PALACE("Tidal Palace", Treasure.THE_OCEANS_CHALICE),
    FOOLS_LANDING("Fool's Landing", null),
    BRONZE_GATE("Bronze Gate", null),
    COPPER_GATE("Copper Gate", null),
    SILVER_GATE("Silver Gate", null),
    GOLD_GATE("Gold Gate", null),
    IRON_GATE("Iron Gate", null),
    CRIMSON_FOREST("Crimson Forest", null),
    DUNES_OF_DECEPTION("Dunes of Deception", null),
    PHANTOM_ROCK("Phantom Rock", null),
    WATCHTOWER("Watchtower", null),
    MISTY_MARSH("Misty Marsh", null),
    OBSERVATORY("Observatory", null),
    TWILIGHT_HOLLOW("Twilight Hollow", null),
    CLIFFS_OF_ABANDON("Cliffs of Abandon", null),
    LOST_LAGOON("Lost Lagoon", null),
    BREAKERS_BRIDGE("Breakers Bridge", null);

    private String name;
    private Treasure treasure;

    TileType(String name, Treasure treasure) {
        this.name = name;
        this.treasure = treasure;
    }

    public String getName() {
        return name;
    }

    public Treasure getTreasure() {
        return treasure;
    }

    public static TileType getStartingTile(Role role) {
        switch (role) {
            case ENGINEER: return BRONZE_GATE;
            case EXPLORER: return COPPER_GATE;
            case DIVER: return IRON_GATE;
            case PILOT: return FOOLS_LANDING;
            case MESSENGER: return SILVER_GATE;
            case NAVIGATOR: return GOLD_GATE;
            default: return FOOLS_LANDING;
        }
    }
}