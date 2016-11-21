import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliakan on 25.03.16.
 */
public class Address {
    protected String index;
    protected String who;
    protected List<String> addressLines;

    public Address() {}

    public String getIndex() {
        return index;
    }

    public String getWho() {
        return who;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setAddress(String address) {
        this.addressLines = new ArrayList<String>();
        this.addressLines.add(address);
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public String toString() {
        return "Address{" +
                "index='" + index + '\'' +
                ", who='" + who + '\'' +
                ", addressLines=" + addressLines +
                '}';
    }
}
