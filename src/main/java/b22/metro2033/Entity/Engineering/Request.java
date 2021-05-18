package b22.metro2033.Entity.Engineering;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "repair_request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String problem;
    private int priority;
    private String description;
    private String location;
    private String commentary;

    @ManyToMany(mappedBy = "requests")
    private List<Engineer> engineers;

    public Request() {
    }

    public Request(long id, String problem, int priority, String description, String location, String commentary) {
        this.id = id;
        this.problem = problem;
        this.priority = priority;
        this.description = description;
        this.location = location;
        this.commentary = commentary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
