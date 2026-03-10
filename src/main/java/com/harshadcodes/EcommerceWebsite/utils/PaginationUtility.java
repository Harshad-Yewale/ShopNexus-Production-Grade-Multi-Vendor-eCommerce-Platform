package com.harshadcodes.EcommerceWebsite.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtility {

    public static Pageable createPageable(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    ){
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber,pageSize,sortByAndOrder);
    }
}
