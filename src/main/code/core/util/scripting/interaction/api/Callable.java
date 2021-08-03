package core.util.scripting.interaction.api;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public interface Callable {
    void action();

    static String getErrorMessage(Response response){
        JSONObject jsonResponse = ApiResponse.getBodyAsJSONObject(response);
        JSONArray jsonArray = jsonResponse.getJSONArray("errors");
        JSONObject jsonResponseMessage =jsonArray.getJSONObject(0);
        String errorMessage = jsonResponseMessage.getString("errorMessage");
        return jsonResponseMessage.getString("errorMessage");
    }

    static String getErrorCode(Response response){
        JSONObject jsonResponse = ApiResponse.getBodyAsJSONObject(response);
        JSONArray jsonArray = jsonResponse.getJSONArray("errors");
        JSONObject jsonResponseMessage =jsonArray.getJSONObject(0);
        return jsonResponseMessage.getString("errorCode");
    }

}
