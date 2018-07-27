package com.design.map;

import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.google.common.collect.Lists;
import org.geojson.GeoJsonObject;
import org.geojson.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Meron Abraha 7/26/18
 */
public class MapTest
{


    public Reference makeReference( final String reference_name, final String referecne_type, final GeoJsonObject geoJsonObject ) {
        return Reference.builder().name( reference_name ).type( referecne_type ).geometry( geoJsonObject ).build();
    }

    public Reference makeReference() {
        return makeReference( "Alba", "Bar", makeGeoJsonObject( -125.00, 45.00 ) );
    }

    public List<Reference> makeReferenceList() {
        Reference referenceA = makeReference( "alba", "TEST1", makeGeoJsonObject( -125.00, 45.00 ) );
        Reference referenceB = makeReference( "id2", "TEST2", makeGeoJsonObject( -90.00, 60.00 ) );

        return Lists.newArrayList( referenceA, referenceB );
    }

    public GeoJsonObject makeGeoJsonObject( final double lng, final double lat ) {
        return new Point( lng, lat );
    }

    public List<Path> makePath() {
        List<Reference> references = makeReferenceList();
        List<Path> paths = new ArrayList();

        for ( Reference reference : references ) {
            paths.add( Path.builder().reference( reference ).distance( 1234567.00 ).build() );
        }

        return paths;
    }
}
