package com.design.map.services;

import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.design.map.exception.BadRequestException;
import com.design.map.exception.NotFoundException;
import com.design.map.repository.ReferenceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther Meron Abraha 7/6/18
 */

@Service
public class DefaultReferenceServices implements ReferenceServices {

    // The acceptable format for the "lnglat" query parameter value, allowing for plus/minus symbols as well as extra spaces.
    private final Pattern longLatPattern = Pattern.compile( "\\s*([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)\\s*" );

    private static final Logger log = LoggerFactory.getLogger( DefaultReferenceServices.class );


    private ReferenceRepository referenceRepository;
    private ObjectMapper referenceObjectMapper;

    @Autowired
    public DefaultReferenceServices( ReferenceRepository referenceRepository,ObjectMapper objectMapper ) {
        this.referenceRepository = referenceRepository;
        this.referenceObjectMapper=objectMapper;

    }

    @Override
    public Reference createReference( Reference reference ) {
        this.referenceRepository.createMap( reference.getName(),reference.getType(), getGeoJson(reference) );
        return this.referenceRepository.readReferenceByName( reference.getName() );
    }

    @Override
    public Reference updateReference( Reference reference ) {
        return null;
    }

    @Override
    public Reference getReferencedName( String name ) {
       Reference reference=referenceRepository.readReferenceByName( name );

        if ( reference == null ) {
            throw new NotFoundException( String.format( "The requested reference was not found for reference name \"%s\" ", name) );
        }

        return reference;
    }

    @Override
    public List<Path> calculatePathByType( String typeName, String lngLat, int limit, int offset ) {

        LngLatAlt lngLatAlt = validateLngLat( lngLat );
        String geoJson = getGeoJsonPoint( new Point( lngLatAlt ) );
        log.info( "calculating distance  geoJson :{}, typeName ,{}",geoJson,typeName );
        return referenceRepository.getPathByType( typeName,geoJson,limit,offset );
    }

    public String getGeoJson( final Reference reference ) {
        String geoJson;
        try {
            geoJson = this.referenceObjectMapper.writeValueAsString( reference.getGeometry() );
        } catch ( JsonProcessingException e ) {
            throw new BadRequestException( "An invalid geometry value was provided.", e );
        }

        return geoJson;
    }


    /**
     * Transforms a {@link Point} to a GeoJson string representation.
     *
     * @param point {@link Point}
     *
     * @return The string representation of GeoJson {@link Point}
     */
    public String getGeoJsonPoint( final Point point ) {
        String geoJson;
        try {
            geoJson = referenceObjectMapper.writeValueAsString( point );
        } catch ( IOException e ) {
            throw new BadRequestException( "Unable to process geometry value.", e );
        }

        return geoJson;
    }


    /**
     * Check that the "lnglat" parameter value contains a valid longitude and latitude, and return as a {@link LngLatAlt}.
     *
     * @param lngLat
     *
     * @return {@link LngLatAlt}
     */
    public LngLatAlt validateLngLat( String lngLat ) {
        Matcher latLongMatcher = longLatPattern.matcher( lngLat );

        if ( !latLongMatcher.matches() ) {
            throw new IllegalArgumentException( "An invalid \"lnglat\" value was provided.  Acceptable format is: [+-]###.######, [+-]##.######" );
        }

        final String lng = latLongMatcher.group( 1 );
        final String lat = latLongMatcher.group( 2 );

        log.info( "Latitude={}, Longitude={}", lat, lng );

        // Latitude values must be between +90 and -90.  Longitude must be between +180 and -180.
        if ( Double.parseDouble( lat ) > 90 || Double.parseDouble( lat ) < -90 ||
             Double.parseDouble( lng ) > 180 || Double.parseDouble( lng ) < -180 ) {
            throw new IllegalArgumentException( "An invalid \"lnglat\" value was provided.  Acceptable ranges are: [+-]180.00, [+-]90.00" );
        }

        return new LngLatAlt( Double.parseDouble( lng ), Double.parseDouble( lat ) );
    }

}
