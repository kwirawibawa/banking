package com.assessment.banking.util;

import lombok.Data;

@Data
public class CommonPaging {
    private int number;
    private int size;
    private long totalElements;

    public CommonPaging(int number, int size, long totalElements) {
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        if (totalElements == 0 || size <= 0) return 0;

        long startIndex = (long) (number - 1) * size;
        if (startIndex >= totalElements) return 0;

        long remaining = totalElements - startIndex;
        return (int) Math.min(remaining, size);
    }

    public int getStartData() {
        if (totalElements == 0) return 0;
        return (number - 1) * size + 1;
    }

    public int getEndData() {
        if (totalElements == 0) return 0;
        return (int) Math.min((long) number * size, totalElements);
    }
}


