package com.design.map.repository;

import com.design.map.MapTest;
import com.design.map.dto.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @auther Meron Abraha 7/25/18
 */

@RunWith(SpringRunner.class)
public class DefaultReferenceRepositoryTest extends MapTest {
    @MockBean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @MockBean
    ObjectMapper objectMapper;

    ReferenceRepository defaultreferenceRepository;

    @Before
    public void init() {
        this.defaultreferenceRepository = new DefaultReferenceRepository( namedParameterJdbcTemplate, objectMapper );
    }

    @Test
    public void readReferenceByName() {
        when( namedParameterJdbcTemplate.queryForObject( anyString(), anyMap(), (RowMapper) anyObject() ) ).thenReturn( makeReference() );
        Reference reference = defaultreferenceRepository.readReferenceByName( "Alba" );
        verify( namedParameterJdbcTemplate, times( 1 ) ).queryForObject( anyString(), anyMap(), (RowMapper) anyObject() );
        assertNotNull( reference );
        assertEquals( reference.getName(), "Alba" );
    }

    @Test
    public void createReference() {
        Map<String, Object> response = new HashMap();
        when( namedParameterJdbcTemplate.update( anyString(), (MapSqlParameterSource) anyObject(), (KeyHolder) anyObject() ) ).thenReturn( 1 );
        Map reference = defaultreferenceRepository.createMap( anyString(), anyString(), anyString() );
        verify( namedParameterJdbcTemplate, times( 1 ) ).update( anyString(),(MapSqlParameterSource) anyObject(), (KeyHolder) anyObject() );
    }

}
