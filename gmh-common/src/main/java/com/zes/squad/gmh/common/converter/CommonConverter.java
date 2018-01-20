package com.zes.squad.gmh.common.converter;

import java.util.List;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;

/**
 * bean拷贝(只支持同名同属性拷贝)
 * 
 * @author yuhuan.zhou 2018年1月13日 下午11:34:40
 */
@Slf4j
public class CommonConverter {

    public static <S, T> T map(S sourceObject, Class<T> targetClass) {
        BeanCopier beanCopier = BeanCopier.create(sourceObject.getClass(), targetClass, false);
        T targetObject = null;
        try {
            targetObject = targetClass.newInstance();
            beanCopier.copy(sourceObject, targetObject, null);
        } catch (Exception e) {
            log.error("bean拷贝异常", e);
        }
        return targetObject;
    }

    public static <S, T> List<T> mapList(List<S> sourceObjects, Class<T> targetClass) {
        BeanCopier beanCopier = BeanCopier.create(sourceObjects.get(0).getClass(), targetClass, false);
        List<T> targetObjects = Lists.newArrayListWithCapacity(sourceObjects.size());
        try {
            for (S sourceObject : sourceObjects) {
                T targetObject = targetClass.newInstance();
                beanCopier.copy(sourceObject, targetObject, null);
                targetObjects.add(targetObject);
            }
        } catch (Exception e) {
            log.error("bean拷贝异常", e);
        }
        return targetObjects;
    }

    public static <S, T> PagedList<T> mapPagedList(PagedList<S> sourcePagedObjects, Class<T> targetClass) {
        return PagedLists.newPagedList(sourcePagedObjects.getPageNum(), sourcePagedObjects.getPageSize(),
                sourcePagedObjects.getTotalCount(), mapList(sourcePagedObjects.getData(), targetClass));
    }

}
