package com.design.map.controller;

/**
 * @auther Meron Abraha 7/24/18
 */

import com.design.map.MapTest;
import com.design.map.controller.MapController;
import com.design.map.dto.NotFoundException;
import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.design.map.services.ReferenceServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.geojson.GeoJsonObject;
import org.geojson.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(MapController.class)
public class ReferenceControllerTest extends MapTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReferenceServices defaultReferenceService;

    @Test
    public void getReferenceByName()
            throws Exception {
        Reference reference = makeReference();
        ObjectMapper mapper = new ObjectMapper();
        String referenceJson = mapper.writeValueAsString( reference );

        given( this.defaultReferenceService.getReferencedName( anyString() ) )
                .willReturn( reference );

        this.mvc.perform( get( "/api/map//{name}", "dadu" )
                                  .param( "limit", "20" )
                                  .param( "offset", "0" )
                                  .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().json( referenceJson ) )
                .andDo( print() );
    }

    @Test
    public void getReferenceByName_NotFound()
            throws Exception {
        given( this.defaultReferenceService.getReferencedName( anyString() ) )
                .willThrow( new NotFoundException( "Not found" ) );

        this.mvc.perform( get( "/api/map/{name}", "TEST" )
                                  .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andDo( print() );
    }

    @Test
    public void createReference()
            throws Exception {
        Reference reference = makeReference();
        ObjectMapper objectMapper=new ObjectMapper();
        String json=objectMapper.writeValueAsString( reference );

        given( this.defaultReferenceService.createReference( any( Reference.class ) ) )
                .willReturn( reference);


        this.mvc.perform( post( "/api/map" )
                                  .content( json )
                                  .accept( MediaType.APPLICATION_JSON )
                                  .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().json( json ) );
    }

    @Test
    public void getNearistDistanceByLocation()
            throws Exception {
        List<Path> paths = makePath();
        ObjectMapper mapper = new ObjectMapper();
        String bathsJson = mapper.writeValueAsString( paths );

        given( this.defaultReferenceService
                       .calculatePathByType( anyString(), anyString(), anyInt(), anyInt() ) )
                .willReturn( paths );

        this.mvc.perform( get( "/api/map/distance" )
                                  .param( "lnglat", "-120.00,40.00" )
                                  .param( "type", "bar" )
                                  .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().json( bathsJson ) )
                .andDo( print() );
    }


}
