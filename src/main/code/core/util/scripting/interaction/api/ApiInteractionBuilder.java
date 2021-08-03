package core.util.scripting.interaction.api;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Header;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import joptsimple.internal.Strings;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import openweather.api.ApiHeader;
import openweather.api.Authenticator;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.stream.Collectors;



public class ApiInteractionBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ApiInteractionBuilder.class);


    public static void fillMissedHeader(Object rawDriver, Object key, Object value) {
        RequestSpecification driver = (RequestSpecification) rawDriver;

        if (!existedHeaderWithName(driver, key)) {
            driver.header(new Header(key.toString(), value.toString()));
        }
    }

    public static void setQueryParam(Object rawDriver, String key, String value) {
        RequestSpecificationImpl driver = (RequestSpecificationImpl) rawDriver;
        driver.removeQueryParam(key);
        driver.queryParam(key, value);
    }

    public static void setQueryParams(Object rawDriver, HashMap<String, String> parametersMap) {
        RequestSpecificationImpl driver = (RequestSpecificationImpl) rawDriver;
        parametersMap.keySet()
                .stream()
        .map(key -> driver.removeQueryParam(key))
                        .collect(Collectors.toList());
        driver.queryParams(parametersMap);
    }

    public static void setFormParam(Object rawDriver, String key, String value) {
        RequestSpecificationImpl driver = (RequestSpecificationImpl) rawDriver;
        driver.removeFormParam(key);
        driver.formParam(key, value);
    }

    public static void setHeader(Object rawDriver, Object key, Object value) {
        RequestSpecification driver = (RequestSpecification) rawDriver;

        removeHeader(driver, key.toString());
        driver.header(new Header(key.toString(), value.toString()));
    }

    public static void setHeader(Object rawDriver, Map<String, Object> headers) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        driver.headers(headers);
    }


    public static void removeHeader(Object rawDriver, Object key) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        if (existedHeaderWithName(driver, key)) {
            ((FilterableRequestSpecification) driver).removeHeader(key.toString());
        }
    }

    private static boolean existedHeaderWithName(Object rawDriver, Object key) {
        RequestSpecificationImpl driver = (RequestSpecificationImpl) rawDriver;
        return driver.getHeaders().hasHeaderWithName(key.toString());
    }

    public static void addMultiPart(Object rawDriver, String controlName, File file) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        driver.multiPart(controlName, file);
    }

    public static void setBody(Object rawDriver, String body) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        driver.body(body);
    }

    public static void setStepUpToken(Object rawDriver, String stepUpToken) {
        RequestSpecification driver = (RequestSpecification) rawDriver;

        setHeader(driver, ApiHeader.STEP_UP_TOKEN.toString(), stepUpToken);
    }

    public static void setAuthorization(Object rawDriver, String authorization) {
        RequestSpecification driver = (RequestSpecification) rawDriver;

        setHeader(driver, ApiHeader.AUTHORIZATION.toString(), authorization);
    }

    public static void resetAuthorization(Object rawDriver, Authenticator.Scheme scheme, String accessToken) {
        RequestSpecification driver = (RequestSpecification) rawDriver;

        String authorization = scheme + accessToken;
        setAuthorization(driver, authorization);
    }

    public static Response call(Object rawDriver, Method method) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        return call(driver, method, true);
    }

    public static Response call(Object rawDriver, Method method, boolean redirect) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        final int BODY_PRINT_LENGTH = 125;
        Response response;
        driver.redirects().follow(redirect);
        driver.relaxedHTTPSValidation();

        RequestSpecificationImpl requester = (RequestSpecificationImpl) driver;
        Options options = Options.builder().printMultiliner()
                .updateCurl(curl -> curl
                        .removeHeader("Host")
                        .removeHeader("User-Agent")
                        .removeHeader("Connection"))
                .build();
        RestAssuredConfig restAssuredConfig = ((RequestSpecificationImpl) driver).getConfig();
        restAssuredConfig = CurlRestAssuredConfigFactory.updateConfig(restAssuredConfig, options);
        driver.config(restAssuredConfig);

        String requestBody = Strings.isNullOrEmpty((String) requester.getBody()) ? "" : requester.getBody().toString();
        requestBody = requestBody.length() < BODY_PRINT_LENGTH ? requestBody : (requestBody.substring(0, BODY_PRINT_LENGTH) + "...");
        LOGGER.info("⬇⬇⬇⬇ Request Details " + requester.toString());
        // Expected a curl log line right after this log line
        switch (method) {
            case GET:
                response = driver.get();
                break;
            case PUT:
                response = driver.put();
                break;
            case POST:
                response = driver.post();
                break;
            case PATCH:
                response = driver.patch();
                break;
            case DELETE:
                response = driver.delete();
                break;
            default:
                response = null;
                break;
        }

        String testLogRequestMessage = String.format("%s %s%s<br/>_____ <i>%s</i>",
                method, requester.getBaseUri(), requester.getBasePath(), StringEscapeUtils.escapeHtml4(requestBody));
        if (response == null) {
            LOGGER.error(testLogRequestMessage + "<br/>Invalid Request Method!");
        } else {
            String responseBody = Strings.isNullOrEmpty(response.asString()) ? "" : response.asString();
            responseBody = responseBody.length() < BODY_PRINT_LENGTH ? responseBody : (responseBody.substring(0, BODY_PRINT_LENGTH) + "...");
            LOGGER.info(
                    String.format("%s<br/><b>%s %s</b> | %s",
                            testLogRequestMessage,
                            response.getStatusCode(), HttpStatus.getStatusText(response.getStatusCode()),
                            StringEscapeUtils.escapeHtml4(responseBody)
                    )
            );
        }
        return response;
    }

    public static void setCookie(Object rawDriver, String cookie) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        driver.cookie(cookie);
    }

    public static RequestSpecificationImpl decorate(Object rawDriver) {
        return (RequestSpecificationImpl) rawDriver;
    }

//    public static Response handle(Object rawDriver, Response response, List successCodes) {
//        return handle(rawDriver, response, successCodes, 0);
//    }
//
//    public static Response handle(Object rawDriver, Response response, List successCodes, int maxRetryTrial) {
//        RequestSpecification driver = (RequestSpecification) rawDriver;
//        LOGGER.debug(String.format("Response Details ⤵\n%s\n%s",
//                "[HEADERS] " + response.getHeaders().toString(),
//                "[BODY] " + response.getBody().asString()
//        ));
//        if (successCodes.contains((Integer) response.getStatusCode())) {
//            // After handle reset driver
//            resetDriver(driver);
//            return onSuccess(response);
//        }
//        if (maxRetryTrial > 0) {
//            response = onRetry(response);
//            // After handle reset driver
//            resetDriver();
//            return handle(response, --maxRetryTrial);
//        } else {
//            // After handle reset driver
//            resetDriver();
//            return onError(response);
//        }
//    }

    public static void setUri(Object rawDriver, String... orderedComponents) {
        RequestSpecification driver = (RequestSpecification) rawDriver;
        String uri = "";
        for (int i = 0; i < orderedComponents.length; i++) {
            uri = uri.concat("/").concat(orderedComponents[i].trim());
        }
        uri = uri.substring(1);
        driver.baseUri(uri);
    }

    public static String getFastestHost(String ... hosts){
        ExecutorService executor = Executors.newCachedThreadPool();
        Collection<Callable> tasks = new ArrayList<>();
        Arrays.stream(hosts).forEach(host -> tasks.add(() -> ApiInteractionBuilder.ping(host)));

        ExecutorCompletionService<?> completion = new ExecutorCompletionService(executor);
        tasks.stream().forEach((task) -> completion.submit(task));
        try {
            Future<?> firstDone;
            do {
                firstDone = completion.take();
            } while (firstDone == null);

            executor.shutdown();
            return (String) firstDone.get();
        } catch (InterruptedException | ExecutionException | NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public static String ping(String URL) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            client.execute(RequestBuilder
                    .get(URL)
                    .setConfig(
                            RequestConfig.custom()
                                    .setConnectTimeout(3000)
                                    .setConnectionRequestTimeout(3000)
                                    .setSocketTimeout(3000)
                                    .build())
                    .build()
            );
            client.close();
            return URL;
        } catch (IOException e) {
            if (e instanceof SSLPeerUnverifiedException) {
                return URL;
            }
            throw e;
        }
    }
}
