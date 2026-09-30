package composants;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class InputPanel extends Panel 
{

    private final String iconPath;
    private final String inputType;

    public InputPanel(String id, IModel<String> model, String iconPath, String inputType) 
    {
        super(id);
        this.iconPath = iconPath;
        this.inputType = inputType;

        TextField<String> textField = new TextField<>("inputField", model);
        textField.add(new AttributeModifier("type", inputType));
        textField.setOutputMarkupId(true);
        add(textField);

        if (iconPath != null && !iconPath.isEmpty()) 
        {
            ResourceReference iconResource = new PackageResourceReference(getClass(), iconPath);
            Image iconImage = new Image("iconImage", iconResource);
            iconImage.setOutputMarkupId(true);
            add(iconImage);
        }
        else 
        {
            add(new WebMarkupContainer("iconImage").setVisible(false));
        }
        
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(InputPanel.class, "../css/InputPanel.css")));
    }

    @Override
    protected void onInitialize() 
    {
        super.onInitialize();
    }
}
