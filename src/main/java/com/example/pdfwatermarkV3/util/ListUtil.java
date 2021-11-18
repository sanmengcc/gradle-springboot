package com.example.pdfwatermarkV3.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {

    /**
     *
     * @param dataList
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partition(final List<T> dataList, final int size) {
        Integer limit = (dataList.size() + size - 1) / size;
        List<List<T>> subList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            subList.add(dataList.stream().skip(i * size).limit(size).collect(Collectors.toList()));
        });
        return subList;
    }
}