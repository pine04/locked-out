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
}