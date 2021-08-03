package core.util.platform.host.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlLoader.class);

    public static Map loadConfig(String filePath) {
        Map settings = new HashMap();
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = YamlLoader.class
                    .getClassLoader()
                    .getResourceAsStream(filePath);
            settings = yaml.load(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            LOGGER.error("!!!!!!!!!! ERROR during loading config file !!!!!!!!!! : " + filePath);
            ex.printStackTrace();
        }
        if (settings == null) return new HashMap<>();
        else return settings;
    }

}
