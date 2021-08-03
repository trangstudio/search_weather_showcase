package openweather.api;

import core.controller.TestController;
import core.util.scripting.interaction.api.ApiInteractionBuilder;
import core.util.scripting.interaction.api.ApiResponse;
import core.util.scripting.interaction.api.Method;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Accessors(fluent = true)
public abstract class ApiInteraction {
    protected final Logger LOGGER = LoggerFactory.getLogger(ApiInteraction.class);

    protected List<Integer> successCodes = new ArrayList<>();
    protected Object driver;

    protected String uri = "";
    @Getter
    @Setter(AccessLevel.PROTECTED)
    protected Method method;
    @Getter
    protected Response response;

    public ApiInteraction() {
        driver = TestController.API().getDriver();
        successCodes.addAll(Arrays.asList(
                HttpStatus.SC_ACCEPTED,
                HttpStatus.SC_OK,
                HttpStatus.SC_MOVED_TEMPORARILY
        ));
    }

    protected void action() {
        response = handle(ApiInteractionBuilder.call(driver, method));
    }

    protected Response handle(Response response) {
        return handle(response, 0);
    }

    protected Response handle(Response response, int maxRetryTrial) {
        Integer responseStatusCode = response.getStatusCode();
        LOGGER.debug(String.format("Response Details ⤵\n%s\n%s",
                "[HEADERS] " + response.getHeaders().toString(),
                "[BODY] " + response.getBody().asString()
        ));
        if (successCodes.contains(responseStatusCode)) {
            return onSuccess(response);
        }
        // TODO review this mechanism
        if (maxRetryTrial > 0) {
            response = onRetry(response);
            // After handle reset driver
            return handle(response, --maxRetryTrial);
        } else {
            return onError(response);
        }
    }

    protected Response onRetry(Response response) {
        // By default, no retry.
        return response;
    }

    private Response onError(Response response) {
        LOGGER.warn("Business Error. Response message: " + response.getBody().asString());
        return response;
    }

    private Response onSuccess(Response response) {
        return response;
    }

    public int getStatusCode() {
        return ApiResponse.getStatusCode(response);
    }


}
