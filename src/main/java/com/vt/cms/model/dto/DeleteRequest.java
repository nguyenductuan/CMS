package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRequest {
    private int userid;
    private List<Integer> productids;
}
