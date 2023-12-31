package org.anagrams.io.impl;

import lombok.RequiredArgsConstructor;
import org.anagrams.exception.BadInputException;
import org.anagrams.model.NormalizedString;
import org.anagrams.service.AnagramsService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

@RequiredArgsConstructor
public class AnagramsInputOutputStreamImpl implements Runnable {
    public static final String REGEX = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
    public static final String PROMPT = "> ";
    public static final String EXITING_TEXT = "Exiting";
    public static final String USAGE_TEXT = """
            Usage:
            To check if two sentences are anagrams write 1 followed by the two sentences.
            Examples:
            \t1 evil vile
            \tOutput: true
            \t1 evil bad
            \tOutput: false
            To retrieve anagrams of a word in the set of inputs of the Feature#1 write 2 followed by that word
            Examples:
            \t2 evil
            \tOutput: [vile]
            \t2 bad
            \tOutput: []

            To exit, press CTRL+D in an empty line.""";

    private final InputStream inputStream;
    private final PrintStream outputStream;
    private final AnagramsService anagramsService;

    @Override
    public void run() {
        printHelp();

        loop();
    }

    private void loop() {
        Scanner scanner = new Scanner(inputStream);
        while (true) {
            outputStream.print(PROMPT);
            int operation;
            try {
                operation = scanner.nextInt();
            } catch (NoSuchElementException e) {
                outputStream.println(EXITING_TEXT);
                return;
            }

            try {
                switch (operation) {
                    case 1 -> executeFeature1(getArgument(scanner), getArgument(scanner));
                    case 2 -> executeFeature2(getArgument(scanner));
                    default -> {
                        outputStream.println("Bad format");
                        printHelp();
                    }
                }
            } catch (BadInputException e) {
                outputStream.println("Bad format");
                printHelp();
            }
        }
    }

    private void executeFeature2(String input) {
        if (input == null) {
            throw new BadInputException();
        }
        outputStream.println(anagramsService.getPreviousAnagrams(new NormalizedString(input)));
    }

    private void executeFeature1(String input1, String input2) {
        if (input1 == null || input2 == null) {
            throw new BadInputException();
        }
        outputStream.println(anagramsService.areAnagrams(new NormalizedString(input1), new NormalizedString(input2)));
    }

    private static String getArgument(Scanner scanner) {
        return scanner.findInLine(REGEX);
    }

    private void printHelp() {
        outputStream.println(USAGE_TEXT);
    }
}
