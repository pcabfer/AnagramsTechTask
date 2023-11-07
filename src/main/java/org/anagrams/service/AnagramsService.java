package org.anagrams.service;

import org.anagrams.model.NormalizedString;

import java.util.Set;

public interface AnagramsService {
    boolean areAnagrams(NormalizedString a, NormalizedString b);

    Set<NormalizedString> getPreviousAnagrams(NormalizedString word);

}
