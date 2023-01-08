package ru.practicum.explorewithme.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends PageRequest {
    protected OffsetPageRequest(int offset, int size, Sort sort) {
        super(offset / size, size, sort);
    }

    public static OffsetPageRequest of(int offset, int size, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Page offset must not be less than zero");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        return new OffsetPageRequest(offset, size, sort);
    }

    public static OffsetPageRequest of(int offset, int size) {
        return of(offset, size, Sort.unsorted());
    }
}
