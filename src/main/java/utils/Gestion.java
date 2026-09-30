package utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Gestion 
{

    private Jdbc jdbc;

    public Gestion() 
    {
        this.jdbc = new Jdbc();
    }

    //Comme son nom l'indique
    public <T> List<T> getObjectsFromTable(String tableName, Class<T> clazz, String whereClause) 
    {
        List<T> objects = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        // Ajouter la clause WHERE si elle est fournie
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            query += " WHERE " + whereClause;
        }

        try 
        {
            ResultSet resultSet = jdbc.executeRequest(query + " ORDER BY id");

            while (resultSet.next()) 
            {
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (Field field : getAllFields(clazz)) 
                {
                    String columnName = field.getName().toLowerCase();

                    if (hasColumn(resultSet, columnName)) 
                    {
                        Object columnValue = resultSet.getObject(columnName);
                        Object convertedValue = convertType(columnValue, field.getType());

                        field.setAccessible(true);
                        field.set(obj, convertedValue);
                    }
                }

                objects.add(obj);
            }
        } catch (Exception e) 
        {
            e.printStackTrace();
        } finally 
        {
            jdbc.disconnect();
        }

        return objects;
    }


    //Comme son nom l'indique
    private List<Field> getAllFields(Class<?> clazz) 
    {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null) 
        {
            for (Field field : currentClass.getDeclaredFields()) 
            {
                fields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    //Regarde si attribut dans la base
    private boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException 
    {
        try 
        {
            resultSet.findColumn(columnName);
            return true;
        } catch (SQLException e) 
        {
            return false;
        }
    }

    //Conversion des valeurs pour etre compatible avec la classe
    private Object convertType(Object value, Class<?> targetType) 
    {
        if (value == null) return null;

        if (targetType == String.class) 
        {
            return value.toString();
        } else if (targetType == Integer.class || targetType == int.class) 
        {
            return ((Number) value).intValue();
        } else if (targetType == Long.class || targetType == long.class) 
        {
            return ((Number) value).longValue();
        } else if (targetType == Float.class || targetType == float.class) 
        {
            return ((Number) value).floatValue();
        } else if (targetType == Double.class || targetType == double.class) 
        {
            return ((Number) value).doubleValue();
        } else if (targetType == java.util.Date.class && value instanceof java.sql.Date) 
        {
            return new java.util.Date(((java.sql.Date) value).getTime());
        } else 
        {
            return value;
        }
    }
}
