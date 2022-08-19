package com.sigma.clotheswarehouse.utils;

import com.sigma.clotheswarehouse.exceptions.PageSizeException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.sigma.clotheswarehouse.utils.AppConstant.MAX_PAGE_SIZE;

public class CommandUtils {

    public static void validatePageAndSize(int page, int size) throws PageSizeException {
        if (page < 0) {
            throw new PageSizeException("Invalid Page number. Page number must not be less than zero!");
        } else if (size <= 0) {
            throw new PageSizeException("Invalid Page size. Page size must not be less than or equal to zero!");
        } else if (size > MAX_PAGE_SIZE) {
            throw new PageSizeException("Invalid Page size. Page size must not be more than " + MAX_PAGE_SIZE + "!");
        }
    }

    public static Pageable simplePageable(int page, int size) throws PageSizeException {
        validatePageAndSize(page, size);
        return PageRequest.of(page, size, Sort.by("id"));
    }

}