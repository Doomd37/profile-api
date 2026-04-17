package com.myproject.profile_api.dto;

import lombok.Data;

@Data
public class GenderResponse {
    private String name;
    private String gender;
    private Double probability;
    private Integer count;
}
