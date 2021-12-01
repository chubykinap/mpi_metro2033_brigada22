package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Army.HealthState;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Engineering.Engineer;
import b22.metro2033.Entity.Engineering.Qualification;
import b22.metro2033.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class EngineerUtility {
    private User user;
    private long id;
    private String qualification;

    private int agility;
    private int strength;
    private int stamina;
    private Post post;
    private String post_location;

    public EngineerUtility(Engineer engineer) {
        this.user = engineer.getUser();
        this.id = engineer.getId();
        this.qualification = Qualification.getStateRU(engineer.getQualification());
    }

    public static List<EngineerUtility> toEngineerUtility(List<Engineer> engineers) {
        List<EngineerUtility> res = new ArrayList<>();
        for (Engineer engineer : engineers) {
            res.add(new EngineerUtility(engineer));
        }
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
