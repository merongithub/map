package com.design.map.repository;

import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.design.map.exception.InternalServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther Meron Abraha 7/9/18
 */
@Repository
public class DefaultReferenceRepository implements ReferenceRepository {
    private static final Logger log = LoggerFactory.getLogger( DefaultReferenceRepository.class );

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public DefaultReferenceRepository( NamedParameterJdbcTemplate namedParameterJdbcTemplate , ObjectMapper objectMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.objectMapper=objectMapper;
    }

    @Override
    public List<Reference> readAllReferences( int limit, int offset ) {
        final Map<String, Object> parameters = new HashMap();
        parameters.put( "limit", limit );
        parameters.put( "offset", offset );

        List<Reference> references = this.namedParameterJdbcTemplate.query(
                "SELECT reference_name,reference_type, ST_AsGeoJSON(shape) as shape " +
                "FROM reference " +
                " WHERE LIMIT :limit " +
                "OFFSET :offset",
                parameters,
                new ReferenceRowMapper() );
        return references;
    }

    @Override
    public Map<String, Object> createMap( String reference_name,String reference_type, String geoJson ) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue( "reference_name", reference_name );
        sqlParameterSource.addValue( "reference_type",reference_type );
        sqlParameterSource.addValue( "geoJson", geoJson );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int success = namedParameterJdbcTemplate.update(
                "INSERT INTO reference (reference_name,reference_type, shape) " +
                "VALUES (:reference_name, :reference_type, ST_GeomFromGeoJSON(:geoJson, 1, 4326))",
                sqlParameterSource, keyHolder );

        log.info( "Insert reference: name={}, geoJson={}, successful: {}", reference_name, geoJson, (success == 1) );

        return keyHolder.getKeys();
    }

    @Override
    public List<Path> getPathByType( String reference_type, String geoJson, int limit, int offset ) {
        final Map<String, Object> parameters = new HashMap();
        parameters.put( "geoJson", geoJson );
        parameters.put( "reference_type", reference_type );
        parameters.put( "limit", limit );
        parameters.put( "offset", offset );

        List<Path> paths = this.namedParameterJdbcTemplate.query(
                "SELECT  reference_type, reference_name, ST_AsGeoJSON(shape) as shape, " +
                "ROUND(ST_Distance_Sphere(shape, ST_GeomFromGeoJSON(:geoJson, 1, 4326)), 2) as dist " +
                "FROM reference " +
                "WHERE reference_type = :reference_type " +
                "ORDER BY dist ASC " +
                "LIMIT :limit " +
                "OFFSET :offset",
                parameters,
                new PathRowMapper() );

        return paths;
    }

    @Override
    public Reference readReferenceByName( String reference_name ) {
        final Map<String, Object> parameters = new HashMap();
        parameters.put( "reference_name", reference_name );
        Reference reference;
        try {
            reference = this.namedParameterJdbcTemplate.queryForObject(
                    "SELECT reference_name,reference_type,ST_AsGeoJSON(shape) as shape " +
                    "FROM reference " +
                    "WHERE reference_name = :reference_name ",
                    parameters,
                    new ReferenceRowMapper() );
        } catch ( EmptyResultDataAccessException e ) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Reference  not found for name={} ", reference_name, e );
            }
            return null;
        }

        return reference;
    }

    public  class ReferenceRowMapper implements RowMapper<Reference> {
        public Reference mapRow( ResultSet resultSet, int rowNum ) {

            try {
                return Reference.builder()
                        .name( resultSet.getString( "reference_name" ) )
                        .type( resultSet.getString( "reference_type" ) )
                        .geometry( objectMapper.readValue( resultSet.getString( "shape" ), GeoJsonObject.class ) )
                        .build();
            } catch ( Exception e ) {
                throw new InternalServerErrorException( e.getMessage() );
            }
        }
    }

    public  class PathRowMapper implements RowMapper<Path> {
        public Path mapRow( ResultSet resultSet, int rowNum ) {
            try {
                return Path.builder()
                        .reference( Reference.builder()

                                           .type( resultSet.getString( "reference_type" ) )
                                           .name( resultSet.getString( "reference_name" ) )
                                           .geometry( objectMapper.readValue( resultSet.getString( "shape" ), GeoJsonObject.class ) )

                                           .build()
                        )
                        .distance( resultSet.getDouble( "dist" ) )
                        .build();
            } catch ( Exception e ) {
                throw new InternalServerErrorException( e.getMessage(), e );
            }
        }
    }

}
