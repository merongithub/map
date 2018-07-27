package com.design.map.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther Meron Abraha 7/16/18
 */
@Data
@NoArgsConstructor
public class GeometryModel {

    @JsonProperty("type")
    private String type;

    @JsonProperty("coordinates")
    private double[] coordinates;

    @Builder
    @JsonCreator
    public GeometryModel(
            @JsonProperty("type")
                    String type,
            @JsonProperty("coordinates")
                    double[] coordinates ) {
        this.type = type;
        this.coordinates = coordinates;
    }
}