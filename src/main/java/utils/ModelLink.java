package utils;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

public class ModelLink 
{
    private final IModel<String> name;
    private final Class<? extends WebPage> pageClass;

    public ModelLink(IModel<String> name, Class<? extends WebPage> pageClass) 
    {
        this.name = name;
        this.pageClass = pageClass;
    }

    public IModel<String> getName() 
    {
        return name;
    }

    public Class<? extends WebPage> getPageClass() 
    {
        return pageClass;
    }
}
