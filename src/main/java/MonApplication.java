import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import pages.HomePage;

public class MonApplication extends WebApplication 
{

    @Override
    public Class<HomePage> getHomePage() 
    {
        return HomePage.class;
    }

    @Override
    public void init() 
    {
        super.init();

        
        // getDebugSettings().setDevelopmentUtilitiesEnabled(false);
        // getDebugSettings().setAjaxDebugModeEnabled(false);
    }

    //This is for deployment mode (à décommenter)
    // @Override
    // public RuntimeConfigurationType getConfigurationType()
    // {
    //     return RuntimeConfigurationType.DEPLOYMENT;
    // }
}
