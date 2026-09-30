package utils;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Jdbc
{
    String url="jdbc:Oracle:thin:@localhost:1521:orcl";
    String user="solar";
    String mdp="nilo";

    Connection connexion = null;
    Statement statement = null;

    public void connect(String ur, String us, String md)
	{

        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connexion=DriverManager.getConnection(ur,us,md);
		}
        catch (ClassNotFoundException | SQLException e)
        {
			e.printStackTrace();
		}
	}

    public void disconnect()
	{
        try
        {
            if(this.statement != null)
            {
                this.statement.close();
            }
            if(this.connexion != null)
            {
                this.connexion.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
	}

    public ResultSet executeRequest(String request) throws Exception
    {

        ResultSet resultat = null;

        try
        {
            connect(this.url, this.user, this.mdp);
            this.connexion.setAutoCommit(false);
            this.statement=this.connexion.createStatement();
            resultat=this.statement.executeQuery(request);
            this.connexion.commit();
		}
        catch (SQLException e)
        {
            if(this.connexion != null)
            {
			    this.connexion.rollback();
            }
            e.printStackTrace();
		}

        return resultat;

    }

    public Jdbc (String us, String pass)
    {
        this.user=us;
        this.mdp=pass;
    }

    public Jdbc (String link, String us, String pass)
    {
        this.url=link;
        this.user=us;
        this.mdp=pass;
    }

    public Jdbc (){}

}