package com.design.map.controller;

import com.design.map.dto.NotFoundException;
import com.design.map.dto.Path;
import com.design.map.dto.Reference;
import com.design.map.services.ReferenceServices;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther Meron Abraha 7/3/18
 */
@RestController
@RequestMapping("/api/map")
public class MapController {
    private static final Logger log = LoggerFactory.getLogger( MapController.class );


    private ReferenceServices referenceServices;

    @Autowired
    public MapController(ReferenceServices referenceServices){
        this.referenceServices=referenceServices;
    }

    @ApiOperation(
            value = "Create reference point",
            notes = "Create reference point",
            code = 201,
            response = Reference.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = Reference.class, message = "Created"),
            @ApiResponse(code = 400, response = Reference.class, message = "Bad request"),
            @ApiResponse(code = 409, response = Reference.class, message = "Conflict"),
            @ApiResponse(code = 500, response = Reference.class, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Reference> createLocation( @ApiParam(name = "reference", required = true)
                                                     @RequestBody Reference reference ) {
        log.info( "Creating a reference ={} ",reference );
        Reference referenceResponse = referenceServices.createReference( reference );
        return ResponseEntity.ok( referenceResponse );
    }

    @ApiOperation(
            value = "Get Reference by name",
            response = Reference.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Reference.class, responseContainer = "List", message = "Ok"),
            @ApiResponse(code = 400, response = Reference.class, message = "Bad request"),
            @ApiResponse(code = 500, response = Reference.class, message = "Internal server error")
    })
    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Reference> getMapByName( @ApiParam(name = "name", example = "Bar Alba", required = true)
                                                   @PathVariable(name = "name") String name,
                                                   @ApiParam(name = "limit", defaultValue = "10")
                                                   @RequestParam(name = "limit", defaultValue = "4", required = false) int limit,
                                                   @ApiParam(name = "offset", defaultValue = "0")
                                                   @RequestParam(name = "offset", defaultValue = "0", required = false) int offset ) {
        log.info( "Limit={}, Offset={}", limit, offset );

        return ResponseEntity.ok( referenceServices.getReferencedName( name ) );
    }


    @ApiOperation(
            value = "Get reference distance by by reference type",
            notes = "Get reference distance by  reference type, source ID and latitude and longitude. " +
                    "The lnglat parameter takes longitude first and latitude second in the following format: ±DDD.DDDD,±DD.DDDD",
            response = Path.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Path.class, message = "Ok"),
            @ApiResponse(code = 400, response = Path.class, message = "Bad request"),
            @ApiResponse(code = 404, response = Path.class, message = "Not found"),
            @ApiResponse(code = 500, response = Path.class, message = "Internal server error")
    })
    @RequestMapping(value = "/distance", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<Path>> getNearistDistanceByType(     @ApiParam(name = "lnglat", examples = @Example(@ExampleProperty("123.4567,-12.3456")), required = true)
                                                                            @RequestParam(name = "lnglat") String lngLat,
                                                                            @ApiParam(name = "type", examples = @Example(@ExampleProperty("123.4567,-12.3456")), required = true)
                                                                            @RequestParam(name = "type") String type ) {
        List<Path> paths = referenceServices.calculatePathByType(type,lngLat,10,0);

        if ( CollectionUtils.isEmpty( paths ) ) {
            throw new NotFoundException( String.format( "The requested reference was not found for reference type \"%s\"", type ) );
        }

        return ResponseEntity.ok( paths);
    }

}
