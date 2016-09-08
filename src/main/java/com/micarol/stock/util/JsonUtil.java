package com.micarol.stock.util;

import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * java对象转化成json字符中的增强类
 * 
 */
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String obj2JsonStr(Object object) {
        // generate json
        String json = null;

        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json != null) {
            return new String(json);
        } else {
            return null;
        }
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T jsonStr2Obj(String jsonStr, Class<T> valueType) {
        try {
            return mapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return null;
    }

    public static String stringify(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return null;
    }

    public static String stringify(Object object, String... properties) {

        try {
            return mapper
                    .writer(new SimpleFilterProvider().addFilter(
                            AnnotationUtils.getValue(
                                    AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).toString(),
                            SimpleBeanPropertyFilter.filterOutAllExcept(properties)))
                    .writeValueAsString(object);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return null;
    }

    public static void stringify(OutputStream out, Object object) {

        try {
            mapper.writeValue(out, object);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public static void stringify(OutputStream out, Object object, String... properties) {

        try {
            mapper.writer(new SimpleFilterProvider().addFilter(
                    AnnotationUtils.getValue(
                            AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).toString(),
                    SimpleBeanPropertyFilter.filterOutAllExcept(properties)))
                    .writeValue(out, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {

        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return null;
    }

    public static JsonNode readTree(String json){
    	try {
			return mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
