package com.design.map.repository;

import com.design.map.dto.Path;
import com.design.map.dto.Reference;

import java.util.List;
import java.util.Map;

/**
 * @auther Meron Abraha 7/6/18
 */
public interface ReferenceRepository {
    List<Reference> readAllReferences( int limit, int offset );

    Reference readReferenceByName(String name);

    Map<String,Object>  createMap( String name, String type, String geoJson );

    List<Path> getPathByType(String typeName, String geoJson,int limit,int offset);

}
