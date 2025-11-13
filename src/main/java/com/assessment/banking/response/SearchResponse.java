package com.assessment.banking.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalDataInPage;
    private Long totalData;
    private Long totalPages;
    private Integer startData;
    private Integer endData;
    private List<T> listData;
    private int code;
    private String message;
}