package com.task.swiftParser.Util;

import java.lang.reflect.Field;
import java.util.Optional;

public class StringUtils {
    public static void capitalizeAllStringFields(Object obj)
    {
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields())
        {
            if (field.getType().equals(String.class))
            {
                field.setAccessible(true);

                try{
                    String value = (String) field.get(obj);
                    Optional.ofNullable(value)
                            .map(String::toUpperCase)
                            .ifPresent(uppercaseValue -> {
                                try{
                                    field.set(obj,uppercaseValue);
                                }
                                catch (IllegalAccessException e)
                                {
                                    System.err.println("Error occurred during setting field value "+ field.getName() + "': " + e.getMessage());
                                }
                            });
                }
                catch (IllegalAccessException e)
                {
                    System.err.println("Error occurred during setting field value "+ field.getName() + "': " + e.getMessage());
                }
            }
        }
    }
}
