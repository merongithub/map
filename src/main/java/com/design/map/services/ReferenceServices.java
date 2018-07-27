package com.design.map.services;

import com.design.map.dto.Path;
import com.design.map.dto.Reference;

import java.util.List;
import java.util.Map;

/**
 * @auther Meron Abraha 7/6/18
 */
public interface ReferenceServices {

    Reference createReference( Reference reference );

    Reference updateReference( Reference reference );

    Reference getReferencedName( String name );
    List<Path> calculatePathByType(String typeName, String lngLat,int limit, int offset);
}
