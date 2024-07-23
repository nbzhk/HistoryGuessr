package org.softuni.finalproject.config.converter;

import org.modelmapper.AbstractConverter;

import java.util.Arrays;

public class ScoreConverter extends AbstractConverter<int[], Integer> {


    @Override
    protected Integer convert(int[] source) {
        int totalScore;

        totalScore =  Arrays.stream(source).sum();

        return totalScore;
    }
}
