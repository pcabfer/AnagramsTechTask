package org.anagrams;

import lombok.RequiredArgsConstructor;
import org.anagrams.io.impl.AnagramsInputOutputStreamImpl;
import org.anagrams.repository.AnagramsRepository;
import org.anagrams.repository.impl.AnagramsRepositoryMapImpl;
import org.anagrams.service.impl.AnagramsServiceImpl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main {

    private static final AnagramsRepository ANAGRAMS_REPOSITORY = new AnagramsRepositoryMapImpl();
    private static final AnagramsServiceImpl ANAGRAMS_SERVICE = new AnagramsServiceImpl(ANAGRAMS_REPOSITORY);
    private final Runnable runnable;

    public Main(InputStream inputStream, PrintStream outputStream) {
        runnable = new AnagramsInputOutputStreamImpl(inputStream, outputStream, ANAGRAMS_SERVICE);
    }

    public static void main(String... args) {
        new Main(System.in, System.out).run();
    }

    private void run() {
        runnable.run();
    }
}