package org.anagrams.integration;

import org.anagrams.Main;
import org.anagrams.io.impl.AnagramsInputOutputStreamImpl;
import org.anagrams.repository.AnagramsRepository;
import org.anagrams.repository.impl.AnagramsRepositoryMapImpl;
import org.anagrams.testutils.TeePrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;

import static java.lang.Boolean.TRUE;
import static org.anagrams.io.impl.AnagramsInputOutputStreamImpl.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTests {
    public static final int PREAMBLE_LENGTH = (USAGE_TEXT + "\n" + PROMPT).length();
    private final AnagramsRepository anagramsRepository = new AnagramsRepositoryMapImpl();
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
        String output = fullOutput.substring(PREAMBLE_LENGTH, fullOutput.length() - ((PROMPT + EXITING_TEXT).length() + 2));
        assertEquals(TRUE.toString(), output);
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
}
