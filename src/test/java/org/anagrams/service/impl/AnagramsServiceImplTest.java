package org.anagrams.service.impl;

import org.anagrams.model.NormalizedString;
import org.anagrams.model.SortedString;
import org.anagrams.repository.AnagramsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnagramsServiceImplTest {

    @Mock
    private AnagramsRepository anagramsRepository;

    @InjectMocks
    private AnagramsServiceImpl anagramsServiceImpl;

    @Test
    void shouldReturnTrue_whenArgumentsAreAnagrams() {
        NormalizedString arg1 = new NormalizedString("vile");
        NormalizedString arg2 = new NormalizedString("evil");

        assertTrue(anagramsServiceImpl.areAnagrams(arg1, arg2));
    }

    @Test
    void shouldReturnFalse_whenArgumentsAreNotAnagrams() {
        NormalizedString arg1 = new NormalizedString("ville");
        NormalizedString arg2 = new NormalizedString("evil");

        assertFalse(anagramsServiceImpl.areAnagrams(arg1, arg2));
    }

    @Test
    void shouldSaveBothArguments_whenAreAnagramsCalled() {
        NormalizedString arg1 = new NormalizedString("vile");
        NormalizedString arg2 = new NormalizedString("evil");

        anagramsServiceImpl.areAnagrams(arg1, arg2);

        SortedString sortedArg1 = new SortedString(arg1.toString());
        SortedString sortedArg2 = new SortedString(arg2.toString());
        verify(anagramsRepository).save(sortedArg1, arg1);
        verify(anagramsRepository).save(sortedArg2, arg2);
    }

    @Test
    void shouldExcludeArgumentFromResults_whenRetrievingFromRepository() {
        Set<NormalizedString> normalizedResultSet = Set.of("vile", "evil", "live")
                .stream()
                .map(NormalizedString::new)
                .collect(Collectors.toSet());
        when(anagramsRepository.findOriginalWordsBySortedWord(any())).thenReturn(Optional.of(normalizedResultSet));

        NormalizedString argument = new NormalizedString("evil");
        Set<NormalizedString> returnedSet = anagramsServiceImpl.getPreviousAnagrams(argument);

        Set<NormalizedString> expectedReturnedSet = normalizedResultSet.stream().filter(not(argument::equals)).collect(Collectors.toSet());
        assertEquals(expectedReturnedSet, returnedSet);
    }
}