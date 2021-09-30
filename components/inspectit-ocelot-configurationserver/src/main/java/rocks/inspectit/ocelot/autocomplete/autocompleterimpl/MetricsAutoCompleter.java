package rocks.inspectit.ocelot.autocomplete.autocompleterimpl;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rocks.inspectit.ocelot.autocomplete.AutoCompleter;
import rocks.inspectit.ocelot.autocomplete.util.ConfigurationQueryHelper;
import rocks.inspectit.ocelot.config.validation.PropertyPathHelper;

import java.util.Collections;
import java.util.List;

import static rocks.inspectit.ocelot.autocomplete.autocompleterimpl.Constants.*;

/**
 * Autocompleter which retrieves declared metric names. This autocompleter is triggered by the paths
 * "inspectit.instrumentation.rules.*.metrics", "inspectit.instrumentation.rules.*.metrics.*.metric" and
 * "inspectit.metrics.definitions".
 */
@Component
public class MetricsAutoCompleter implements AutoCompleter {

    @Autowired
    private ConfigurationQueryHelper configurationQueryHelper;

    /**
     * The path under which metric names are defined.
     */
    private final static List<String> METRICS_DECLARATION_PATH = ImmutableList.of(INSPECTIT, METRICS, "definitions");

    /**
     * All paths under which metric names are used.
     */
    private static final List<List<String>> RULE_SUGGESTION_PATHS = ImmutableList.of(
            // @formatter:off
            METRICS_DECLARATION_PATH,
            ImmutableList.of(INSPECTIT, INSTRUMENTATION, RULES, STAR, METRICS),
            ImmutableList.of(INSPECTIT, INSTRUMENTATION, RULES, STAR, METRICS, STAR, METRIC)
            // @formatter:on
    );

    /**
     * Checks if the given path leads to a metric Attribute, e.g. "inspectit.instrumentation.rules.*.metrics" and returns
     * all declared metrics that ca be used in this path as a List of Strings.
     *
     * @param path A given path as List. Each String should act as a literal of the path.
     *
     * @return A List of Strings containing all declared metrics that could be used with the given path.
     */
    @Override
    public List<String> getSuggestions(List<String> path) {
        boolean matches = RULE_SUGGESTION_PATHS.stream()
                .anyMatch(metricPath -> PropertyPathHelper.comparePaths(path, metricPath));
        if (matches) {
            return configurationQueryHelper.getKeysForPath(METRICS_DECLARATION_PATH);
        }
        return Collections.emptyList();
    }
}
