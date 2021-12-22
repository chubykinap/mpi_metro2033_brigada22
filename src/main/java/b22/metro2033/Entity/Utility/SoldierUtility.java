package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Army.HealthState;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class SoldierUtility {
    private User user;
    private long id;
    private String rank;
    private String health_state;
    private int agility;
    private int strength;
    private int stamina;
    private Post post;
    private String post_location;

    public SoldierUtility(Soldier soldier) {
        this.user = soldier.getUser();
        this.id = soldier.getId();
        this.rank = Rank.getStateRU(soldier.getRank());
        this.health_state = HealthState.getStateRU(soldier.getHealth_state());
        this.agility = soldier.getCharacteristics().getAgility();
        this.strength = soldier.getCharacteristics().getStrength();
        this.stamina = soldier.getCharacteristics().getStamina();
        this.post = soldier.getPost();
    }

    public static List<SoldierUtility> toSoldierUtility(List<Soldier> soldiers) {
        List<SoldierUtility> res = new ArrayList<>();
        for (Soldier soldier : soldiers) {
            res.add(new SoldierUtility(soldier));
        }
        return res;
    }

    public static SoldierUtility toSoldierUtilityOne(Soldier soldier){
        SoldierUtility soldierUtility = new SoldierUtility(soldier);
        return soldierUtility;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getHealth_state() {
        return health_state;
    }

    public void setHealth_state(String health_state) {
        this.health_state = health_state;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public String getPost_location() {
        return post_location;
    }

    public void setPost_location(String post_location) {
        this.post_location = post_location;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
