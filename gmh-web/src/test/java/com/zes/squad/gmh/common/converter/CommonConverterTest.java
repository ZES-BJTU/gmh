package com.zes.squad.gmh.common.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.entity.SourceObject;
import com.zes.squad.gmh.common.entity.TargetObject;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;

public class CommonConverterTest {

    @Test
    public void testMap() {
        SourceObject sourceObject = new SourceObject();
        Long sourceId = 1L;
        String sourceName = "source";
        sourceObject.setId(sourceId);
        sourceObject.setName(sourceName);
        TargetObject targetObject = CommonConverter.map(sourceObject, TargetObject.class);
        assertNotNull(targetObject);
        assertEquals(sourceId, targetObject.getId());
        assertEquals(sourceName, targetObject.getName());
    }

    @Test
    public void testMapList() {
        SourceObject sourceObject = new SourceObject();
        Long sourceId = 1L;
        String sourceName = "source";
        sourceObject.setId(sourceId);
        sourceObject.setName(sourceName);
        List<SourceObject> sourceObjects = Lists.newArrayListWithCapacity(1);
        sourceObjects.add(sourceObject);
        List<TargetObject> targetObjects = CommonConverter.mapList(sourceObjects, TargetObject.class);
        assertNotNull(targetObjects);
        assertEquals(1, targetObjects.size());
        assertNotNull(targetObjects.get(0));
        assertEquals(sourceId, targetObjects.get(0).getId());
        assertEquals(sourceName, targetObjects.get(0).getName());
    }

    @Test
    public void testMapPagedList() {
        SourceObject sourceObject = new SourceObject();
        Long sourceId = 1L;
        String sourceName = "source";
        sourceObject.setId(sourceId);
        sourceObject.setName(sourceName);
        List<SourceObject> sourceObjects = Lists.newArrayListWithCapacity(1);
        sourceObjects.add(sourceObject);
        int pageNum = 1;
        int pageSize = 10;
        long totalCount = 1L;
        PagedList<SourceObject> sourcePagedObjects = PagedLists.newPagedList(1, 10, 1, sourceObjects);
        PagedList<TargetObject> targetPagedObjects = CommonConverter.mapPagedList(sourcePagedObjects,
                TargetObject.class);
        assertNotNull(targetPagedObjects);
        assertNotNull(targetPagedObjects);
        assertEquals(pageNum, targetPagedObjects.getPageNum());
        assertEquals(pageSize, targetPagedObjects.getPageSize());
        assertEquals(totalCount, targetPagedObjects.getTotalCount());
        assertNotNull(targetPagedObjects.getData());
        assertEquals(1, targetPagedObjects.getData().size());
        assertNotNull(targetPagedObjects.getData().get(0));
        assertEquals(sourceId, targetPagedObjects.getData().get(0).getId());
        assertEquals(sourceName, targetPagedObjects.getData().get(0).getName());
    }

}
