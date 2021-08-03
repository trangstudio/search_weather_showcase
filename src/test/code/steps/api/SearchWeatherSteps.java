package steps.api;

import core.util.platform.environment.TestEnvironment;
import core.util.scripting.assertion.Verify;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import openweather.TestEnvSetting;
import openweather.api.external.SearchWeatherHashMap;
import openweather.api.external.WeatherParam;

import java.util.HashMap;

public class SearchWeatherSteps {
    SearchWeatherHashMap searchWeatherHashMap;
    HashMap<String,String> weather = new HashMap<>();

    @Given("User search weather by {string} with API")
    public void userSearchWeatherByCityName(String cityName) {
        weather.put(WeatherParam.valueOfLabel("CityName"), cityName);
        searchWeatherHashMap = new SearchWeatherHashMap(weather,(String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Given("User search weather by city ID {string} with API")
    public void userSearchWeatherByCityCode(String cityCode) {
        weather.put(WeatherParam.valueOfLabel("CityId"), cityCode);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Then("User receive result by {string} with {string} and {string} with API")
    public void userReceiveStatusCode(String type, String statusCode, String responseName) {
        if (searchWeatherHashMap.getStatusCode() == Integer.parseInt(statusCode)) {
            switch (type){
                case "city name" :
                    case "geographic coordinates":
                case "zip code":
                    if (!searchWeatherHashMap.getDetail().getString("name").isEmpty()) {
                        Verify.hardAssertEqual(searchWeatherHashMap.getDetail().getString("name"), responseName);
                        Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForOneLocationCityName(), "Schema for " + responseName + " is correctly");
                    } else {
                        System.out.println("Response data is empty");
                    }
                    break;
                case "city id":
                    if (!searchWeatherHashMap.getDetail().getString("name").isEmpty()) {
                        Verify.hardAssertEqual(searchWeatherHashMap.getDetail().getString("name"), responseName);
                        Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForOneLocationCityId(), "Schema for " + responseName + " is correctly");
                    } else {
                        System.out.println("Response data is empty");
                    }
                    break;

            }
        }
    }


    @Then("User receive result by city ID with {string} and {string} with API")
    public void userReceiveStatusCodeForCityId(String statusCode, String responseName) {
        if (searchWeatherHashMap.getStatusCode() == Integer.parseInt(statusCode)) {
            if (!searchWeatherHashMap.getDetail().getString("name").isEmpty()) {
                Verify.hardAssertEqual(searchWeatherHashMap.getDetail().getString("name"), responseName);
                Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForOneLocationCityId(), "Schema for " + responseName + " is correctly");
            } else {
                System.out.println("Response data is empty");
            }
        }
    }

    @Given("User search weather by {string} and {string} with API")
    public void userSearchWeatherByCityNameAndStateCode(String cityName, String stateCode) {
        weather.put(WeatherParam.valueOfLabel("CityName"), cityName + "," + stateCode);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Given("User search weather by {string} and {string} and {string} with API")
    public void userSearchWeatherByCityNameStateCodeCountryCode(String cityName, String stateCode, String countryCode) {
        weather.put(WeatherParam.valueOfLabel("CityName"), cityName + "," + stateCode + "," + countryCode);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Given("User search weather by zip code {string} and {string} with API")
    public void userSearchWeatherByZipCodeCountryCode(String zipCode, String countryCode) {
        weather.put(WeatherParam.valueOfLabel("Zipcode"), zipCode + "," + countryCode);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Given("User search weather by geographic coordinates {string} and {string} with API")
    public void userSearchWeatherByCityCode(String latitude, String longitude) {
        weather.put(WeatherParam.valueOfLabel("Latitude"), latitude);
        weather.put(WeatherParam.valueOfLabel("Longitude"), longitude);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.SINGLE_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Given("User search weather by rectangle zone {string} with API")
    public void userSearchWeatherByRectangleZone(String rectangleZoneValue) {
        weather.put(WeatherParam.valueOfLabel("Rectangle"), rectangleZoneValue);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.MULTI_LOCATION_PARAM));
        searchWeatherHashMap.action();
    }

    @Then("User receive result for several cities with {string} with API")
    public void userReceiveResultForSeveralCitiesWithStatusCode(String statusCode) {
        if (searchWeatherHashMap.getStatusCode() == Integer.parseInt(statusCode)) {
                Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForMultiLocation(), "Schema for several cities  is correctly");
        }
    }

    @Then("User receive result by {string} for several cities with {string} with API")
    public void userReceiveResultForSeveralCitiesInCircleWithStatusCode(String type, String statusCode) {
        if (searchWeatherHashMap.getStatusCode() == Integer.parseInt(statusCode)) {
            switch (type) {
                case "rectangle zone":
                    Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForMultiLocation(), "Schema for several cities  in rectangle zone is correctly");
                    break;

                case "circle":
                    Verify.softAssertTrue(searchWeatherHashMap.validateSchemaForMultiLocationCircle(), "Schema for several cities in circle  is correctly");
                    break;
            }
        }
    }

    @Given("User search weather by circle with  {string} and {string} and {string} with API")
    public void userSearchWeatherByCityCircle(String latitude, String longitude, String numberOfCities) {
        weather.put(WeatherParam.valueOfLabel("Latitude"), latitude);
        weather.put(WeatherParam.valueOfLabel("Longitude"), longitude);
        weather.put(WeatherParam.valueOfLabel("NumberOfCity"), numberOfCities);
        searchWeatherHashMap = new SearchWeatherHashMap(weather, (String) TestEnvironment.getValue(TestEnvSetting.MULTI_LOCATION_CIRCLE_PARAM));
        searchWeatherHashMap.action();
    }
}
