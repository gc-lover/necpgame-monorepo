package com.necpgame.backjava.util;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Pageable реализация с поддержкой произвольного offset.
 */
public class OffsetBasedPageRequest implements Pageable, Serializable {

    private final long offset;
    private final int pageSize;
    private final Sort sort;

    public OffsetBasedPageRequest(long offset, int pageSize, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        this.offset = offset;
        this.pageSize = pageSize;
        this.sort = sort == null ? Sort.unsorted() : sort;
    }

    public OffsetBasedPageRequest(long offset, int pageSize) {
        this(offset, pageSize, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / pageSize);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(offset + pageSize, pageSize, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        long newOffset = Math.max(offset - pageSize, 0);
        return new OffsetBasedPageRequest(newOffset, pageSize, sort);
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, pageSize, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        return new OffsetBasedPageRequest((long) pageNumber * pageSize, pageSize, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > 0;
    }
}

