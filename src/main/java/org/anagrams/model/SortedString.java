package org.anagrams.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class SortedString {
    public final String sortedString;

    public SortedString(String str) {
        this.sortedString = str.chars()
                .filter(codePoint -> !Character.isWhitespace(codePoint))
                .map(Character::toLowerCase)
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public String toString() {
        return sortedString;
    }
}
