package com.design.map.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @auther Meron Abraha 7/12/18
 */
@EqualsAndHashCode
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Path {

    @JsonProperty("reference")
    private Reference reference;

    @JsonSerialize(using = ReferenceDoubleDeserializer.class )
    @JsonProperty("distance")
    private Double distance;

    @JsonProperty("distanceUnits")
    private String distanceUnits;

    @Builder
    @JsonCreator
    public Path(
            @JsonProperty("reference")
                    Reference reference,
            @JsonProperty("distance")
                    Double distance,
            @JsonProperty("distanceUnits")
                    String distanceUnits ) {
        this.reference = reference;
        this.distance = distance;
        this.distanceUnits = (StringUtils.isEmpty( distanceUnits ) ? "meters" : distanceUnits );
    }

    /**
     * This deserializer is used to prevent distance from being converted to scientific notation.
     */
    public static class ReferenceDoubleDeserializer extends JsonSerializer<Double> {

        @Override
        public void serialize( Double value, JsonGenerator gen, SerializerProvider serializers )
                throws IOException {
            gen.writeNumber( String.format( "%.2f", value ) );
        }
    }
}
