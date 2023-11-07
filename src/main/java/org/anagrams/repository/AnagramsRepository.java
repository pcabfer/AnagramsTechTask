package org.anagrams.repository;

import org.anagrams.model.NormalizedString;
import org.anagrams.model.SortedString;

import java.util.Optional;
import java.util.Set;

public interface AnagramsRepository {
    Optional<Set<NormalizedString>> findOriginalWordsBySortedWord(SortedString sortedWord);

    void save(SortedString sortedWord, NormalizedString originalWord);
}
