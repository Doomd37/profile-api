package com.myproject.profile_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class NationalizeResponse {
    private String name;
    private List<CountryData> country;
}
