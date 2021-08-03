package core.util.scripting.io;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonHelper {

    public static JSONObject getFirst(String KEY, String value, JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString(KEY).equals(value)) {
                    return array.getJSONObject(i);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JSONObject();
    }

    public static ArrayList<JSONObject> toArrayList(JSONArray array) {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        array.forEach(jsonObject -> arrayList.add(((JSONObject) jsonObject)));
        return arrayList;
    }

    public static JSONArray buildJSONArray(List<Map> list) {
        JSONArray jsonArray = new JSONArray();
        list.forEach((object) -> {
            ((Map<String, Object>) object).forEach((key, value) -> {
                jsonArray.put(new JSONObject().put(key, value));
            });
        });
        return jsonArray;
    }

    public static JSONObject merge(JSONObject... objects) {
        JSONObject merged = new JSONObject();
        for (JSONObject object : objects) {
            for (String key : JSONObject.getNames(object)) {
                merged.put(key, object.get(key));
            }
        }
        return merged;
    }

    public static JSONObject convert(String source, SourceFormat format) {
        switch (format) {
            case XML:
                return XML.toJSONObject(source);
        }
        return new JSONObject();
    }

    public enum SourceFormat {
        XML
    }
}
