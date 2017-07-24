package com.shenxy.smartapploader.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luosong on 14-4-17.
 */
public class JSONExtension {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //caolei, 空字符串可以转换为空对象
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private JSONExtension() {}

    public static final <T> T convertValue(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    public static final <T> T parseObject(String text, TypeReference<T> type) {
        try {
            return mapper.readValue(text, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static final <T> T parseObject(String text, Class<T> clazz) {
        try {
//            if (JSONSmartWrapper.class.isAssignableFrom(clazz)) {
//                T result = clazz.newInstance();
//                ((JSONSmartWrapper) result).setJSONObject(JSONValue.parse(text));
//            }
        	if(text==null)
        		return null;
            ObjectReader reader = mapper.reader(clazz);
            return reader.readValue(text);
        } catch (IOException e) {
            return null;
//        } catch (InstantiationException e) {
//            return null;
//        } catch (IllegalAccessException e) {
//            return null;
        }
    }

    public static final <T> T parseObject(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        try {
            JavaType javaType =  mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(text, javaType);
        } catch (IOException e) {
            return null;
        }
    }


    public static final List<?> parseArray(String text) {
        try {
            JavaType javaType =  mapper.getTypeFactory().constructParametricType(ArrayList.class, Object.class);
            return mapper.readValue(text, javaType);
        } catch (IOException e) {
            return null;
        }
    }


    public static final String toJSONString(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
//        return JSONValue.toJSONString(object);
    }

    public static final byte[] toJSONBytes(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static final void writeJSONString(Object object, Appendable appendable) {
        try {
            appendable.append(toJSONString(object));
        } catch (IOException e) {
        }
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is a boolean or
     * can be coerced to a boolean. Returns false otherwise.
     */
    public static boolean optBoolean(Map<String, Object> map, String name) {
        return optBoolean(map, name, false);
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is a boolean or
     * can be coerced to a boolean. Returns {@code fallback} otherwise.
     */
    public static boolean optBoolean(Map<String, Object> map, String name, boolean fallback) {
        Object object = map.get(name);
        Boolean result = toBoolean(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is a double or
     * can be coerced to a double. Returns {@code NaN} otherwise.
     */
    public static double optDouble(Map<String, Object> map, String name) {
        return optDouble(map, name, Double.NaN);
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is a double or
     * can be coerced to a double. Returns {@code fallback} otherwise.
     */
    public static double optDouble(Map<String, Object> map, String name, double fallback) {
        Object object = map.get(name);
        Double result = toDouble(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is an int or
     * can be coerced to an int. Returns 0 otherwise.
     */
    public static int optInt(Map<String, Object> map, String name) {
        return optInt(map, name, 0);
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is an int or
     * can be coerced to an int. Returns {@code fallback} otherwise.
     */
    public static int optInt(Map<String, Object> map, String name, int fallback) {
        Object object = map.get(name);
        Integer result = toInteger(object);
        return result != null ? result : fallback;
    }


    /**
     * Returns the value mapped by {@code name} if it exists and is a long or
     * can be coerced to a long. Returns 0 otherwise.
     */
    public static long optLong(Map<String, Object> map, String name) {
        return optLong(map, name, 0L);
    }

    /**
     * Returns the value mapped by {@code name} if it exists and is a long or
     * can be coerced to a long. Returns {@code fallback} otherwise.
     */
    public static long optLong(Map<String, Object> map, String name, long fallback) {
        Object object = map.get(name);
        Long result = toLong(object);
        return result != null ? result : fallback;
    }


    /**
     * Returns the value mapped by {@code name} if it exists, coercing it if
     * necessary. Returns the empty string if no such mapping exists.
     */
    public static String optString(Map<String, Object> map, String name) {
        return optString(map, name, "");
    }

    /**
     * Returns the value mapped by {@code name} if it exists, coercing it if
     * necessary. Returns {@code fallback} if no such mapping exists.
     */
    public static String optString(Map<String, Object> map, String name, String fallback) {
        Object object = map.get(name);
        String result = toString(object);
        return result != null ? result : fallback;
    }

    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    public static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }
}
