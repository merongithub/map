package com.design.map.services;

import com.design.map.MapTest;
import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.design.map.exception.NotFoundException;
import com.design.map.repository.ReferenceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @auther Meron Abraha 7/25/18
 */
@RunWith(SpringRunner.class)
public class ReferenceServicesTest extends MapTest {

    @MockBean
    ReferenceRepository referenceRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    ReferenceServices defaultRefereceServices;

    private String geoJson;

    @Before
    public void init()
            throws IOException {
        this.geoJson = objectMapper.writeValueAsString( makeGeoJsonObject( -120, 20 ) );
        this.defaultRefereceServices = new DefaultReferenceServices( referenceRepository, objectMapper );
    }

    @Test
    public void getReferenceByNameTest() {
        when( referenceRepository.readReferenceByName( anyString() ) ).thenReturn( makeReference() );
        Reference reference = defaultRefereceServices.getReferencedName( "Alba" );
        verify( referenceRepository, times( 1 ) ).readReferenceByName( anyString() );
        assertNotNull( reference );
        assertEquals( reference.getName(), "Alba" );
    }

    @Test(expected = NotFoundException.class)
    public void testCreateLocation_NotFound() {
        Reference reference = makeReference();
        when( referenceRepository.readReferenceByName( anyString() ) ).thenReturn( null );
        defaultRefereceServices.getReferencedName( anyString() );
    }

    @Test
    public void createReferenceTest() {
        when( referenceRepository.createMap( anyString(), anyString(), anyString() ) ).thenReturn( new HashMap<String, Object>() );
        when( referenceRepository.readReferenceByName( anyString() ) ).thenReturn( makeReference() );
        Reference reference = defaultRefereceServices.createReference( makeReference() );
        verify( referenceRepository, times( 1 ) ).createMap( anyString(), anyString(), anyString() );
        verify( referenceRepository, times( 1 ) ).readReferenceByName( anyString() );
    }

    @Test
    public void calculatePathTest()
            throws Exception {
        when( referenceRepository.getPathByType( anyString(), anyString(), anyInt(), anyInt() ) ).thenReturn( makePath() );
        String latLang = "-125.00, -90.00";
        List<Path> pathList = defaultRefereceServices.calculatePathByType( "bar", latLang, 10, 0 );
        GeoJsonObject geoJsonObject = makeGeoJsonObject( -125.00, -90.0 );
        String geoJson = objectMapper.writeValueAsString( geoJsonObject );
        verify( referenceRepository, times( 1 ) ).getPathByType( "bar", geoJson, 10, 0 );
        assertNotNull( pathList );
    }
}
