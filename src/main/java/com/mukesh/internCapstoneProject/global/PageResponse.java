package com.mukesh.internCapstoneProject.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    T data;
    int page;
    int size;
    int totalPages;
    long totalElements;
    boolean isLastPage;
}
