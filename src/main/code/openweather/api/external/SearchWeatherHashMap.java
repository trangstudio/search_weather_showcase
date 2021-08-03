package openweather.api.external;

import core.util.platform.environment.TestEnvironment;
import core.util.scripting.interaction.api.ApiInteractionBuilder;
import core.util.scripting.interaction.api.ApiResponse;
import core.util.scripting.interaction.api.Method;
import openweather.TestEnvSetting;
import openweather.api.ApiInteraction;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchWeatherHashMap extends ApiInteraction {
    private static final String SEARCH_ONE_LOCATION_CITY_NAME_SCHEMA = "/openweather/searchweather/search_one_location_city_name_schema.json";
    private static final String SEARCH_ONE_LOCATION_CITY_ID_SCHEMA = "/openweather/searchweather/search_one_location_city_name_schema.json";
    private static final String SEARCH_MULTI_LOCATION_SCHEMA = "/openweather/searchweather/search_multi_location_schema.json";
    private static final String SEARCH_MULTI_LOCATION_CIRCLE_SCHEMA = "/openweather/searchweather/search_multi_location_circle_schema.json";

    private HashMap<String, String> weatherMap;
    private String paramURL;


    public SearchWeatherHashMap(HashMap<String, String> weatherMap, String paramURL){
        this.method = Method.GET;
        this.weatherMap = weatherMap;
        this.paramURL = paramURL;
    }

        @Override
    public void action() {
        uri = (String) TestEnvironment.getValue(TestEnvSetting.OPEN_WEATHER_DOMAIN_API) + paramURL;
        ApiInteractionBuilder.setUri(driver,uri);
        configureQueryParams();
        ApiInteractionBuilder.setQueryParam(driver, "appid", (String) TestEnvironment.getValue(TestEnvSetting.OPEN_WEATHER_DOMAIN_APP_ID));
        super.action();
    }

    private void configureQueryParams(){
        ApiInteractionBuilder.setQueryParams(driver, weatherMap);
    }

    public JSONObject getDetail() {
        return ApiResponse.getBodyAsJSONObject(response);
    }

    public boolean validateSchemaForOneLocationCityName() {
        return ApiResponse.validateSchema(ApiResponse.getBodyAsJSONObject(response), SEARCH_ONE_LOCATION_CITY_NAME_SCHEMA);
    }

    public boolean validateSchemaForOneLocationCityId() {
        return ApiResponse.validateSchema(ApiResponse.getBodyAsJSONObject(response), SEARCH_ONE_LOCATION_CITY_ID_SCHEMA);
    }

    public boolean validateSchemaForMultiLocation() {
        return ApiResponse.validateSchema(ApiResponse.getBodyAsJSONObject(response), SEARCH_MULTI_LOCATION_SCHEMA);
    }

    public boolean validateSchemaForMultiLocationCircle() {
        return ApiResponse.validateSchema(ApiResponse.getBodyAsJSONObject(response), SEARCH_MULTI_LOCATION_CIRCLE_SCHEMA);
    }
}
