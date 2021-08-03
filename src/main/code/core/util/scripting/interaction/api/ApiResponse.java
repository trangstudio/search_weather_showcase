package core.util.scripting.interaction.api;

import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ApiResponse {
    public static Object getValueFromBodyAsJsonWithJsonPath(Response response, String jsonPath) {
        return response.getBody().jsonPath().get(jsonPath);
    }

    public static ArrayList getJsonArrayFromResponse(Response response, String jsonPath) {
        return response.getBody().jsonPath().get(jsonPath);
    }

    public static String getHeaderValue(Response response, Object headerKey) {
        return response.getHeader(headerKey.toString());
    }

    public static JSONObject getBodyAsJSONObject(Response response) {
        return new JSONObject(response.getBody().asString());
    }

    public static JSONArray getBodyAsJSONArray(Response response) {
        return new JSONArray(response.getBody().asString());
    }

    public String getHeaderValueFromHeadersResponse(Response response, String headerKey) {
        return response.getHeaders().getValue(headerKey);
    }

    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static boolean validateSchema(JSONObject response, String schemaPath) {
        try {
            JSONObject jsonSchema = new JSONObject(new JSONTokener(ApiResponse.class.getResourceAsStream(schemaPath)));
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(response);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
