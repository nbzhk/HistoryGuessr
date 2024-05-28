package org.softuni.finalproject.config;

import org.modelmapper.AbstractConverter;

import java.util.List;
import java.util.stream.IntStream;

public class ScoreConverter extends AbstractConverter<int[], List<Integer>> {

    @Override
    protected List<Integer> convert(int[] source) {
        return IntStream.of(source).boxed().toList();
    }
}
