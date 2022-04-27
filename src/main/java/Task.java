import java.util.List;

/**
 * Created by iliakan on 25.03.16.
 */
public class Task {

    protected List<Address> toList;
    protected Address from;

    public List<Address> getToList() {
        return toList;
    }

    public void setToList(List<Address> toList) {
        this.toList = toList;
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "toList=" + toList +
                ", from=" + from +
                '}';
    }
}
