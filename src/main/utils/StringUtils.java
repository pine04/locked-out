package utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static String joinWithAnd(List<String> nouns) {
        if (nouns.isEmpty()) {
            return "";
        }

        if (nouns.size() == 1) {
            return nouns.getFirst();
        }

        if (nouns.size() == 2) {
            return String.join(" and ", nouns);
        }

        return String.join(", ", nouns.subList(0, nouns.size() - 1)) + ", and " + nouns.getLast();
    }

    public static List<String> splitAndDedupe(String inputString, String separator) {
        return Arrays.stream(inputString.split(separator))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();
    }
}
