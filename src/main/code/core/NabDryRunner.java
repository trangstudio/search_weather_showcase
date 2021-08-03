package core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static core.RunnerConfiguration.Glue;
import static core.RunnerConfiguration.Plugin;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {Plugin.JSON, Plugin.PRETTY, Plugin.PLAIN_HTML},
        glue = {Glue.CORE, Glue.STEPS},
        monochrome = true,
        dryRun = true
)
public class NabDryRunner {
}
