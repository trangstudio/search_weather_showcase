package core;

public interface RunnerConfiguration {
    interface Glue {
        String CORE = "core/controller";
        String STEPS = "steps";
    }

    interface Plugin {
        String PRETTY = "pretty";
        String PLAIN_HTML = "html:target/test-reports/cucumber/cucumber.html";
        String JSON = "json:target/test-reports/cucumber/cucumber.json";
    }
}
