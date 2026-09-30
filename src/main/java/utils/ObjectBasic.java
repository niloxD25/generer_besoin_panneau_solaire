package utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class ObjectBasic implements Serializable 
{

    private int id;
    private String name;
    private String desce;

    public ObjectBasic(int i, String n, String d) 
    {
        this.id = i;
        this.name = n;
        this.desce = d;
    }

    public ObjectBasic(String name, String desce) 
    {
        this.name = name;
        this.desce = desce;
    }

    public ObjectBasic(int i)
    {
        this.id = i;
    }

    public ObjectBasic() {}

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getDesce() 
    {
        return desce;
    }

    public void setDesce(String desce) 
    {
        this.desce = desce;
    }

    // Méthode pour insérer l'objet dans la base de données
    public int insert(Jdbc jdbc, String tableName)
    {
        //Récupérer les colonnes de la table dans la base de données
        Set<String> tableColumns = new HashSet<>();
        try 
        {
            jdbc.connect(jdbc.url, jdbc.user, jdbc.mdp);
            ResultSet columns = jdbc.connexion.getMetaData().getColumns(null, jdbc.user.toUpperCase(), tableName.toUpperCase(), null);
            while (columns.next()) 
            {
                tableColumns.add(columns.getString("COLUMN_NAME").toLowerCase());
                System.out.println(columns.getString("COLUMN_NAME").toLowerCase());
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
            return 0;
        } finally 
        {
            jdbc.disconnect();
        }
    
        List<String> fieldNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();
    
        //Filtrer les champs de la classe pour correspondre aux colonnes de la base
        for (Field field : getAllFields(this.getClass())) 
        {
            try 
            {
                field.setAccessible(true);
                String fieldName = field.getName().toLowerCase();

                //Exclure id car auto-increment
                if (!fieldName.equals("id") && tableColumns.contains(fieldName)) 
                {
                    fieldNames.add(fieldName);
                    values.add(field.get(this));
                }
            } catch (IllegalAccessException e) 
            {
                e.printStackTrace();
                return 0;
            }
        }
    
        //Construire et exécuter la requête d'insertion
        String sql = "INSERT INTO " + tableName + " (" + String.join(", ", fieldNames) + ") VALUES (" + generatePlaceholders(fieldNames.size()) + ")";
        int generatedId = 0;

        try 
        {
            jdbc.connect(jdbc.url, jdbc.user, jdbc.mdp);
            jdbc.statement = jdbc.connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < values.size(); i++) 
            {
                ((PreparedStatement) jdbc.statement).setObject(i + 1, values.get(i));
            }

            ((PreparedStatement) jdbc.statement).executeUpdate();

            generatedId = getId(tableName, jdbc);

        } catch (SQLException e) 
        {
            e.printStackTrace();
            return 0;
        } finally 
        {
            jdbc.disconnect();
        }

        return generatedId;

    }

    public int getId(String table, Jdbc jdbc)
    {
        int id = 0;

        String request="SELECT MAX(ID) as id FROM " + table;
        ResultSet ans=null;
        try {
            ans=jdbc.executeRequest(request);

            if (ans.next()) 
            {
                id = ans.getInt("id");
            } 
            else 
            {
                throw new Exception("Aucune valeur trouvee");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            // throw e;
        }
        finally {
            jdbc.disconnect();
        }
        return id;
    }

    // Méthode pour mettre à jour l'objet dans la base de données
    public boolean update(Jdbc jdbc, String tableName) 
    {
        //Récupérer les colonnes de la table dans la base de données
        Set<String> tableColumns = new HashSet<>();
        try 
        {
            jdbc.connect(jdbc.url, jdbc.user, jdbc.mdp);
            ResultSet columns = jdbc.connexion.getMetaData().getColumns(null, jdbc.user.toUpperCase(), tableName.toUpperCase(), null);
            while (columns.next()) 
            {
                tableColumns.add(columns.getString("COLUMN_NAME").toLowerCase());
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        } finally 
        {
            jdbc.disconnect();
        }
    
        List<String> fieldNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();
    
        //Filtrer les champs de la classe pour correspondre aux colonnes de la base
        for (Field field : getAllFields(this.getClass())) 
        {
            try 
            {
                field.setAccessible(true);
                String fieldName = field.getName().toLowerCase();
                
                //Exclure id car auto-increment
                if (!fieldName.equals("id") && tableColumns.contains(fieldName)) 
                {
                    fieldNames.add(fieldName);
                    values.add(field.get(this));
                }

            } catch (IllegalAccessException e) 
            {
                e.printStackTrace();
                return false;
            }
        }
    
        //Construire et exécuter la requête de mise à jour
        if (fieldNames.isEmpty()) 
        {
            System.out.println("Aucun champ à mettre à jour.");
            return false;
        }
    
        String setClause = String.join(", ", fieldNames.stream().map(f -> f + " = ?").toArray(String[]::new));
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";
        try 
        {
            jdbc.connect(jdbc.url, jdbc.user, jdbc.mdp);
            jdbc.statement = jdbc.connexion.prepareStatement(sql);
    
            for (int i = 0; i < values.size(); i++) 
            {
                ((PreparedStatement) jdbc.statement).setObject(i + 1, values.get(i));
            }
            // Ajouter l'ID à la fin de la liste des paramètres
            ((PreparedStatement) jdbc.statement).setObject(values.size() + 1, this.id);
    
            int rowsUpdated = ((PreparedStatement) jdbc.statement).executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        } finally 
        {
            jdbc.disconnect();
        }
    }    

    // Méthode pour supprimer l'objet dans la base de données
    public boolean delete(Jdbc jdbc, String tableName) 
    {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try 
        {
            jdbc.connect(jdbc.url, jdbc.user, jdbc.mdp);
            jdbc.statement = jdbc.connexion.prepareStatement(sql);
            ((PreparedStatement) jdbc.statement).setInt(1, this.id);

            ((PreparedStatement) jdbc.statement).executeUpdate();
            return true;

        } catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        } finally 
        {
            jdbc.disconnect();
        }
    }

    // Méthode pour obtenir tous les champs de la classe, y compris ceux des superclasses
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

    // Méthode pour générer les placeholders "?" pour l'insertion
    private String generatePlaceholders(int count) 
    {
        String[] placeholders = new String[count];
        for (int i = 0; i < count; i++) 
        {
            placeholders[i] = "?";
        }
        return String.join(", ", placeholders);
    }
}
