package org.anagrams.integration;

import org.anagrams.Main;
import org.anagrams.testutils.TeePrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static org.anagrams.io.impl.AnagramsInputOutputStreamImpl.EXITING_TEXT;
import static org.anagrams.io.impl.AnagramsInputOutputStreamImpl.PROMPT;
import static org.anagrams.io.impl.AnagramsInputOutputStreamImpl.USAGE_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    private final PrintStream STDOUT = System.out;
    private final InputStream STDIN = System.in;


    @AfterEach
    void tearDown() {
        System.setIn(STDIN);
        System.setOut(STDOUT);
    }

    @ParameterizedTest
    @CsvSource({"\"New York Times\",\"monkeys write\"", "evil,vile", "\"Tom Marvolo Riddle\",\"I am Lord Voldemort\""})
    void shouldPrintTrue_whenArgumentsAreAnagrams(String arg1, String arg2) {
        String command = "1 " + arg1 + " " + arg2;
        System.setIn(new ByteArrayInputStream(command.getBytes()));


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new TeePrintStream(STDOUT, outputStream));

        Main.main();

        String fullOutput = outputStream.toString();
        String output = getOnlyResultText(fullOutput).get(0);
        assertTrue(Boolean.parseBoolean(output));
    }

    @ParameterizedTest
    @MethodSource
    void shouldPrintPreviouslyIntroducedWordsThatAreAnagramsOfGivenArgument_whenThereIsAny(String command, int numCommands, String expectedResult) {
        // String command = "1 evil vile\n1 live eat\n2 vile";
        System.setIn(new ByteArrayInputStream(command.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new TeePrintStream(STDOUT, outputStream));

        Main.main();

        String fullOutput = outputStream.toString();
        String output = getOnlyResultText(fullOutput).get(numCommands - 1);
        assertEquals(expectedResult, output);
    }

    @Test
    void shouldPrintOnlyBrackets_whenThereIsNoAnagramInPreviouslyIntroducedWords() {
        String command = "1 evil vile\n1 live eat\n2 eat";
        System.setIn(new ByteArrayInputStream(command.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new TeePrintStream(STDOUT, outputStream));

        Main.main();

        String fullOutput = outputStream.toString();
        String output = getOnlyResultText(fullOutput).get(2);
        assertEquals("[]", output);
    }

    @Test
    void shouldPrintUsage_whenApplicationStarts() {
        String command = "";
        System.setIn(new ByteArrayInputStream(command.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new TeePrintStream(STDOUT, output));

        Main.main();

        assertEquals(USAGE_TEXT + "\n" + PROMPT + EXITING_TEXT + "\n", output.toString());
    }

    private static Stream<Arguments> shouldPrintPreviouslyIntroducedWordsThatAreAnagramsOfGivenArgument_whenThereIsAny() {
        return Stream.of(
                Arguments.of("1 evil vile\n1 live eat\n2 vile", 3, "[evil, live]"),
                Arguments.of("1 \"New York Times\" \"monkeys write\"\n2 \"monkeys write\"", 2, "[New York Times]")
        );
    }

    private List<String> getOnlyResultText(String output) {
        final String filteredOutConstants = skipExitingText(skipPrompt(skipPreamble(output)));
        return Arrays.stream(filteredOutConstants.split(System.lineSeparator()))
                .filter(not(String::isBlank))
                .toList();
    }

    private String skipPreamble(String output) {
        return output.replace(USAGE_TEXT, "");
    }

    private String skipExitingText(String output) {
        return output.replace(EXITING_TEXT, "");
    }

    private String skipPrompt(String output) {
        return output.replace(PROMPT, "");
    }
}
