package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Soldier;

import java.util.ArrayList;
import java.util.List;

public class PostUtility {
    private long id;
    private String name;
    private String location;
    private List<Soldier> soldier;
    private List<MovementSensor> movementSensors;

    public PostUtility(Post post) {
        this.id = post.getId();
        this.name = post.getName();
        this.location = post.getLocation();
        this.soldier = post.getSoldier();
        this.movementSensors = post.getMovementSensors();
    }

    public static List<PostUtility> toPostUtility(List<Post> posts) {
        List<PostUtility> res = new ArrayList<>();
        for (Post post : posts) {
            res.add(new PostUtility(post));
        }
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Soldier> getSoldier() {
        return soldier;
    }

    public void setSoldier(List<Soldier> soldier) {
        this.soldier = soldier;
    }

    public List<MovementSensor> getMovementSensors() {
        return movementSensors;
    }

    public void setMovementSensors(List<MovementSensor> movementSensors) {
        this.movementSensors = movementSensors;
    }
}
