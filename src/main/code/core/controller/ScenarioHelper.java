package core.controller;

import io.cucumber.java.Scenario;
import org.assertj.core.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ScenarioHelper {
    public static String getTagSingleValue(Scenario scenario, Tag tagName) {
        return getTagSingleValue(scenario, tagName.toString());
    }

    public static String getTagSingleValue(Scenario scenario, String tagName) {
        Collection<String> matchedTags = getTagMultipleValues(scenario, tagName, null);
        if (matchedTags == null || matchedTags.isEmpty()) return null;
        else {
            return matchedTags.iterator().next();
        }
    }

    public static Collection<String> getTagMultipleValues(Scenario scenario, Tag tagName, String delimiter) {
        return getTagMultipleValues(scenario, tagName.toString(), delimiter);
    }


    public static Collection<String> getTagMultipleValues(Scenario scenario, String tagName, String delimiter) {
        Collection<String> tags = scenario.getSourceTagNames();
        Collection<String> matchedTags = new ArrayList<>();
        if (tags.isEmpty()) return null;

        for (String tag : tags) {
            if (tag.contains(tagName)) {
                if (Strings.isNullOrEmpty(delimiter)) {
                    matchedTags.add(tag.replace(tagName, ""));
                } else {
                    matchedTags.addAll(Arrays.asList(
                            tag.replace(tagName, "").split(delimiter).clone()
                    ));
                }
            }
        }
        return matchedTags;
    }
}
