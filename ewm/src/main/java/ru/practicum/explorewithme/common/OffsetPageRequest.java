package ru.practicum.explorewithme.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Allows using PageRequest with entry offset instead of full page offset
 */
public class OffsetPageRequest extends PageRequest {
    protected OffsetPageRequest(int offset, int size, Sort sort) {
        super(offset / size, size, sort);
    }

    /**
     * Creates a new {@link OffsetPageRequest} with sort parameters applied.
     * @param offset entry index, must not be negative.
     * @param size the size of the page to be returned, must be positive.
     * @param sort must not be {@literal null}.
     */
    public static OffsetPageRequest of(int offset, int size, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Page offset must not be less than zero");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
        return new OffsetPageRequest(offset, size, sort);
    }

    /**
     * Creates a new unsorted {@link OffsetPageRequest}.
     * @param offset entry index, must not be negative.
     * @param size the size of the page to be returned, must be positive.
     */
    public static OffsetPageRequest of(int offset, int size) {
        return of(offset, size, Sort.unsorted());
    }
}
