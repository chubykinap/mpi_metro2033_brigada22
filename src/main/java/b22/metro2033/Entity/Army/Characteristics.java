package b22.metro2033.Entity.Army;

import javax.persistence.*;

@Entity
@Table(name = "soldier_characteristics")
public class Characteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int agility;
    private int strength;
    private int stamina;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "soldier_id")
    private Soldier soldier;

    public Characteristics() {
    }

    public Characteristics(int agility, int strength, int stamina) {
        this.agility = agility;
        this.strength = strength;
        this.stamina = stamina;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
