package org.anagrams.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class NormalizedString {

    private final String normalizedString;

    public NormalizedString(String str) {
        this.normalizedString = str.strip().replaceAll("[^A-z ]", "").toLowerCase();
    }

    @Override
    public String toString() {
        return this.normalizedString;
    }
}
