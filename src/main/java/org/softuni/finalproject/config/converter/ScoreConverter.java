package org.softuni.finalproject.config.converter;

import org.modelmapper.AbstractConverter;

import java.util.Arrays;
import java.util.List;

public class ScoreConverter extends AbstractConverter<int[], List<Integer>> {


    @Override
    protected List<Integer> convert(int[] source) {
        List<Integer> result;

        result =  Arrays.stream(source)
                .boxed()
                .toList();

        return result;
    }
}
