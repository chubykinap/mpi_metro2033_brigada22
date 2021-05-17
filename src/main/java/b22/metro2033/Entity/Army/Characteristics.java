package b22.metro2033.Entity.Army;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "soldier_characteristics")
public class Characteristics {
    @Id
    private int id;
    private int agility;
    private int strength;
    private int stamina;

    @OneToOne(mappedBy = "characteristics")
    private Soldier soldier;

    public Characteristics() {
    }

    public Characteristics(int agility, int strength, int stamina) {
        this.agility = agility;
        this.strength = strength;
        this.stamina = stamina;
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
