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
 * This AutoCompleter retrieves all rules which can be found in the present yaml-files. It is triggered by
 * the path "inspectit.instrumentation.rules".
 */
@Component
public class RuleAutoCompleter implements AutoCompleter {

    @Autowired
    private ConfigurationQueryHelper configurationQueryHelper;

    /**
     * The path under which rule names are defined.
     */
    private static final List<String> RULE_DEFINITION_PATH = ImmutableList.of(INSPECTIT, INSTRUMENTATION, RULES);

    /**
     * All paths under which rule names are used.
     */
    private static final List<List<String>> RULE_SUGGESTION_PATHS = ImmutableList.of(
            // @formatter:off
            RULE_DEFINITION_PATH,
            ImmutableList.of(INSPECTIT, INSTRUMENTATION, RULES, STAR, "include")
            // @formatter:on
    );

    /**
     * Checks if the given path leads to a rule Attribute, e.g. "inspectit.instrumentation.rules" and returns
     * all declared rules that could be used in this path as  List of Strings.
     *
     * @param path A given path as List. Each String should act as a literal of the path.
     *
     * @return A List of Strings containing all declared rules that could be used with the given path.
     */

    @Override
    public List<String> getSuggestions(List<String> path) {
        boolean matches = RULE_SUGGESTION_PATHS.stream()
                .anyMatch(rulePath -> PropertyPathHelper.comparePaths(path, rulePath));
        if (matches) {
            return configurationQueryHelper.getKeysForPath(RULE_DEFINITION_PATH);
        }
        return Collections.emptyList();
    }
}
