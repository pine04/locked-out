package utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StringUtilsTest {
    @Test
    public void givenEmptyList_whenJoinWithAnd_shouldReturnEmptyString() {
        String output = StringUtils.joinWithAnd(List.of());

        assertEquals("", output);
    }

    @Test
    public void givenListWithOneNoun_whenJoinWithAnd_shouldReturnNoun() {
        String output = StringUtils.joinWithAnd(List.of("apple"));

        assertEquals("apple", output);
    }

    @Test
    public void givenListWithTwoNouns_whenJoinWithAnd_shouldReturnNounsJoinedByAnd() {
        String output = StringUtils.joinWithAnd(List.of("apple", "orange"));

        assertEquals("apple and orange", output);
    }

    @Test
    public void givenListWithManyNouns_whenJoinWithAnd_shouldReturnCorrectString() {
        String output = StringUtils.joinWithAnd(List.of("apple", "orange", "banana", "pear"));

        assertEquals("apple, orange, banana, and pear", output);
    }

    @Test
    public void givenStringOfDuplicatedSubstrings_whenNormalizeAndDedupe_shouldReturnDedupedListOfSubstrings() {
        List<String> output = StringUtils.splitAndDedupe("apple,apple,apple,orange,orange,grape", ",");

        assertEquals(List.of("apple", "orange", "grape"), output);
    }

    @Test
    public void givenStringWithExcessSpaces_whenNormalizeAndDedupe_shouldReturnListOfStrippedSubstrings() {
        List<String> output = StringUtils.splitAndDedupe("  apple  ,  orange  ,   grape  ", ",");

        assertEquals(List.of("apple", "orange", "grape"), output);
    }

    @Test
    public void givenStringWithBlanks_whenNormalizeAndDedupe_shouldListOfSubstringsWithoutBlanks() {
        List<String> output = StringUtils.splitAndDedupe(" ,apple, ,orange, , ,grape, , ", ",");

        assertEquals(List.of("apple", "orange", "grape"), output);
    }

    @Test
    public void givenEmptyString_whenCapitalize_shouldReturnEmptyString() {
        assertEquals("", StringUtils.capitalize(""));
    }

    @Test
    public void givenBlankString_whenCapitalize_shouldReturnSameString() {
        assertEquals("   ", StringUtils.capitalize("   "));
    }

    @Test
    public void givenUncapitalizedString_whenCapitalize_shouldReturnCapitalizedString() {
        assertEquals("Hello world!", StringUtils.capitalize("hello world!"));
    }

    @Test
    public void givenCapitalizedString_whenCapitalize_shouldReturnSameString() {
        assertEquals("Hello world!", StringUtils.capitalize("Hello world!"));
    }
}