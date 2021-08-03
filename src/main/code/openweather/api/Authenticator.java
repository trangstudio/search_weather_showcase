package openweather.api;

public interface Authenticator {
    String getAccessToken();

    enum Scheme {
        Basic("Basic "),
        Bearer("Bearer ");

        protected String key;

        Scheme(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return this.key;
        }
    }
}
