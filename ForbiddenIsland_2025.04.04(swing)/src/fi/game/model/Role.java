package fi.game.model;

/**
 * 角色枚举
 */
public enum Role {
    ENGINEER("Engineer", "May shore up 2 tiles for 1 action"),
    EXPLORER("Explorer", "May move and shore up diagonally"),
    DIVER("Diver", "May swim to the nearest tile"),
    PILOT("Pilot", "May fly to any tile once per turn for 1 action"),
    MESSENGER("Messenger", "May give cards without being on the same tile"),
    NAVIGATOR("Navigator", "May move other players up to 2 adjacent tiles per action");

    private String title;
    private String ability;

    Role(String title, String ability) {
        this.title = title;
        this.ability = ability;
    }

    public String getTitle() {
        return title;
    }

    public String getAbility() {
        return ability;
    }
}