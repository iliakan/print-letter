import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliakan on 25.03.16.
 */
public class Address {
    protected String index;
    protected List<String> addressLines;
    protected List<String> whoLines;

    public Address() {}

    public Address(String index, List<String> whoLines, List<String> addressLines) {
        this.index = index;
        this.whoLines = whoLines;
        this.addressLines = addressLines;
    }

    public String getIndex() {
        return index;
    }

    public List<String> getWhoLines() {
        return whoLines;
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
        this.whoLines = new ArrayList<String>();
        this.whoLines.add(who);
    }

    public void setWhoLines(List<String> whoLines) {
        this.whoLines = whoLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public String toString() {
        return "Address{" +
                "index='" + index + '\'' +
                ", whoLines='" + whoLines + '\'' +
                ", addressLines=" + addressLines +
                '}';
    }
}
