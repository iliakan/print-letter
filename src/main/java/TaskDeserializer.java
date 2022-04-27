import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliakan on 20/08/16.
 */
public class TaskDeserializer implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        final Task task = new Task();

        JsonObject fromJson = json.getAsJsonObject().get("from").getAsJsonObject();
        JsonArray toListJson = json.getAsJsonObject().get("toList").getAsJsonArray();


        JsonArray addressLinesJson = fromJson.get("addressLines").getAsJsonArray();
        List<String> addressLines = new ArrayList<String>();

        for (int i = 0; i < addressLinesJson.size(); i++) {
            String line = addressLinesJson.get(i).getAsString();
            addressLines.add(line);
        }

        List<String> whoLines = new ArrayList<String>();
        whoLines.add(fromJson.get("who").getAsString());

        task.setFrom(new Address(
                fromJson.get("index").getAsString(),
                whoLines,
                addressLines
        ));


        List<Address> toList = new ArrayList<Address>();


        for (int i = 0; i < toListJson.size(); i++) {
            JsonObject toListItem = toListJson.get(i).getAsJsonObject();
            Address address = new Address();
            address.setAddress(toListItem.get("address").getAsString());
            address.setIndex(toListItem.get("index").getAsString());
            address.setWho(toListItem.get("who").getAsString());
            toList.add(address);
        }

        task.setToList(toList);

        return task;
    }
}
