package org.anagrams.service.impl;

import lombok.RequiredArgsConstructor;
import org.anagrams.model.NormalizedString;
import org.anagrams.model.SortedString;
import org.anagrams.repository.AnagramsRepository;
import org.anagrams.service.AnagramsService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class AnagramsServiceImpl implements AnagramsService {
    private final AnagramsRepository anagramsRepository;

    @Override
    public boolean areAnagrams(NormalizedString a, NormalizedString b) {
        final SortedString sortedA = new SortedString(a.toString());
        anagramsRepository.save(sortedA, a);

        final SortedString sortedB = new SortedString(b.toString());
        anagramsRepository.save(sortedB, b);

        return sortedA.equals(sortedB);
    }

    @Override
    public Set<NormalizedString> getPreviousAnagrams(NormalizedString word) {
        return anagramsRepository.findOriginalWordsBySortedWord(new SortedString(word.toString()))
                .map(originalWords -> originalWords.stream().filter(not(word::equals)).collect(Collectors.toSet()))
                .orElseGet(HashSet::new);
    }

}
