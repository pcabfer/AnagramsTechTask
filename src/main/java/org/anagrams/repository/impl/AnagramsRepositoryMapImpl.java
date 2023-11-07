package org.anagrams.repository.impl;

import org.anagrams.model.NormalizedString;
import org.anagrams.model.SortedString;
import org.anagrams.repository.AnagramsRepository;

import java.util.*;

public class AnagramsRepositoryMapImpl implements AnagramsRepository {
    private final Map<SortedString, Set<NormalizedString>> anagrams = new HashMap<>();

    @Override
    public Optional<Set<NormalizedString>> findOriginalWordsBySortedWord(SortedString sortedWord) {
        return Optional.ofNullable(anagrams.get(sortedWord));
    }

    @Override
    public void save(SortedString sortedWord, NormalizedString originalWord) {
        anagrams.computeIfAbsent(sortedWord, k -> new HashSet<>()).add(originalWord);
    }
}
