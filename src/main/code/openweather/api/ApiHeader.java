package openweather.api;

import io.restassured.http.Header;

public enum ApiHeader {
    ACCEPT("Accept"),
    ACCEPTED_ENCODING("Accept-Encoding"),
    APP_CODE("appCode"),
    AUTHORIZATION_MEDIUM("AUTHORIZATION_MEDIUM"),
    AUTHORIZATION("Authorization"),
    BASIC("Basic "),
    BEARER("Bearer "),
    CACHE_CONTROL("cache-control"),
    CALLER_ID("Caller-Id"),
    CHANNEL("Channel"),
    CONNECTION("Connection"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("cookie"),
    CORRELATION_ID("Correlation-Id"),
    DELIVERY_METHOD("Delivery-Method"),
    DEVICE_ID_WSO2_IS("Device_ID"),
    DEVICE_ID("Device-Id"),
    DEVICE_INFO("Device-Info"),
    DEVICE_NAME("Device-Name"),
    DEVICE_OS("Device-OS"),
    ORIGINATION_SESSION_ID("sessionId"),
    PROFILE_ID("Profile-Id"),
    RECAPTCHA_TOKEN("RecaptchaToken"),
    REQUEST_TOKEN("Request-Token"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SESSION_ID("Session-Id"),
    SOAP_ACTION("SOAPAction"),
    STEP_UP_TOKEN("Step-Up-Token"),
    TIME_STAMP("Timestamp"),
    USER_AGENT("User-Agent"),
    STORE_ID("Store-Id"),
    RETAILER_ID("Retailer-Id");

    private final String key;

    ApiHeader(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public static Header build(ApiHeader headerKey, Object headerValue) {
        return new Header(headerKey.toString(), headerValue.toString());
    }


    public enum Value {
        APPLICATION_JSON("application/json"),
        APPLICATION_JSON_UTF8("application/json;charset=UTF-8"),
        APPLICATION_WWW_FORM_URLENCODED_UTF8("application/x-www-form-urlencoded;charset=UTF-8"),
        APPLICATION_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
        APPLICATION_XML("application/xml"),
        MULTIPART_FORM_DATA("multipart/form-data"),
        AUTHORIZATION_MEDIUM_CBSA_AUTH_SAID("cbsa-auth-said"),
        CALLER_ID_WEB("Channel.Web"),
        CHANNEL_WEB("Channel.Web"),
        SEC_FETCH_MODE_VALUE("cors"),
        CLIENT_CREDENTIALS("client_credentials"),
        DEFAULT_DELIVERY_METHOD("SMS"),
        DEFAULT_SESSION_ID("1d948834-a1de-11e7-abc4-cec278b6b50a"),
        DEFAULT_CORRELATION_ID("96dc6517-b024-4ec4-9437-fa4b00bc7a7d"),
        TIMES_TAMP("1540448976234"),
        NO_CACHE("no-cache"),
        SMS("SMS"),
        TEXT_PLAIN("text/plain"),
        TEXT_XML("text/xml"),
        KEEP_ALIVE("keep-alive"),
        GZIP_DEFLATE("gzip, deflate"),
        RECAPTCHA_TOKEN("6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe"),
        DEFAULT_DEVICE_INFO("eyJkZXZpY2VfaWQiOiI1MTVlNDU1OCIsImRldmljZV9uYW1lIjoic2Ftc3VuZyBTTS1QNTU1IiwiZGV2aWNlX29zIjoiQW5kcm" +
                "9pZCA3LjEuMSIsImdlb2xvY2F0aW9uIjoiIiwibWFjIjoiMDI6MDA6MDA6MDA6MDA6MDAiLCJzaW1faW5mbyI6W3siaW1laSI6IjM1O" +
                "Tg5OTA2MjUzNTU0OSIsImltc2kiOiIiLCJuZXR3b3JrIjoiIiwic2ltX2lkIjoiIn1dfQ"),
        ;

        private final String value;

        Value(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
