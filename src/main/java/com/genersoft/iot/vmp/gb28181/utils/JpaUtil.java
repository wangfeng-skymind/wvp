package com.genersoft.iot.vmp.gb28181.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangfeng
 * @date 2025/5/12
 * @description TODO
 */

@Slf4j
public class JpaUtil {


    //将查询出来的值赋予更新的要更新的对象
    //若更新的对象的其中的属性为空，则将数据库查出来的值赋予给你更新对象
    @SneakyThrows
    public static void copyNotNullProperties2src(Object arg, Object db) {


        Object domain = null;
        Class domainClass = arg.getClass();
        Method method = null;

        Class targetClass = db.getClass();

        Field[] fields = domainClass.getDeclaredFields();

        for(Field field: fields) {
            field.setAccessible(true);//允许获取私有属性的值
            String name = field.getName();
            Object inputValue = field.get(arg);

            //log.info("name={},value={}", name, value);
            if(ObjectUtils.isEmpty(inputValue)){// || value == Integer.valueOf(0) || value == Long.valueOf(0)) {
                continue;
            }
            Field targetField = targetClass.getDeclaredField(name);  // name属性不存在时候报错java.lang.NoSuchFieldException: name
            targetField.setAccessible(true);//允许获取私有属性的值
            //将不为空的参数值，更新到数据库查出来的实体中，
            targetField.set(db, inputValue);

        }
    }




    /**
     * @param src
     * @param target
     * @name 排除指定字段
     */
    public static void copyNotNullPropertiesExclude(Object src, Object target, String[] Field) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, Field, true));
    }

    /**
     * @param src
     * @param target
     * @name 允许指定字段
     */
    public static void copyNotNullPropertiesAllow(Object src, Object target, String[] excludeField) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, excludeField, false));
    }


    /**
     * @param src
     * @param target
     * @name 允许指定字段
     */
    public static void copyPropertiesAllow(Object src, Object target, String[] excludeField) {
        BeanUtils.copyProperties(src, target, getPropertyNames(src, excludeField, false));
    }

    /**
     * @param source
     * @return
     * @name 筛选忽略字段
     */
    public static String[] getNullPropertyNames(Object source, String[] excludeField, boolean tag) {
        String[] result = null;
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("baseData")) {
                continue;
            } else {
                Object srcValue = src.getPropertyValue(pd.getName());
                //pd.getName() , 存在 Allow ,Exclude
                boolean contains = Arrays.asList(excludeField).contains(pd.getName());
                if (srcValue == null || contains == tag) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }



    /**
     * @param source
     * @return
     * @name 筛选忽略字段 ， 不去掉null
     */
    public static String[] getPropertyNames(Object source, String[] excludeField, boolean tag) {
        String[] result = null;
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("baseData")) {
                continue;
            } else {
                Object srcValue = src.getPropertyValue(pd.getName());
                //pd.getName() , 存在 Allow ,Exclude
                boolean contains = Arrays.asList(excludeField).contains(pd.getName());
                if (contains == tag) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
