package com.design.map.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.geojson.GeoJsonObject;

/**
 * @auther Meron Abraha 7/6/18
 */

@EqualsAndHashCode
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reference {
    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private GeoJsonObject geometry;

    @Builder
    @JsonCreator
    public Reference(
            @JsonProperty("name")
             String name,
            @JsonProperty("type")
            String type,
            @JsonProperty("geometry")
            GeoJsonObject geometry ) {
         this.geometry = geometry;
         this.name=name;
         this.type=type;
    }
}
