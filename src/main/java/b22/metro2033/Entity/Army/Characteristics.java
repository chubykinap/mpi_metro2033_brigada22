package b22.metro2033.Entity.Army;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "soldier_characteristics")
public class Characteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(value = 0,message = "value should not be less than 0")
    @Max(value = 100,message = "value should not be more than 100 ")
    private int agility;
    @Min(value = 0,message = "value should not be less than 0")
    @Max(value = 100,message = "value should not be more than 100 ")
    private int strength;
    @Min(value = 0,message = "value should not be less than 0")
    @Max(value = 100,message = "value should not be more than 100 ")
    private int stamina;

    @OneToOne
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

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

}