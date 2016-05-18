import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apeacock on 5/17/16.
 */
public class Status {

    private String id = "";
    private List<HashMap<String, Object>> data = new ArrayList<>();

    public Status(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        id = json.getString("id");
        data = (ArrayList<HashMap<String, Object>>) json.get("data");
    }

    public Double getAmountByRecipient(String userId) {
        for(HashMap<String, Object> map : data) {
            if(map.get("recipient").equals(userId)) {
                return (Double) map.get("amount");
            }
        }
        return null;
    }

    @Override
    public String toString() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", id);
        jsonMap.put("data", data);
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

}
