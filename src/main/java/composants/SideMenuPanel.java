// src/main/java/com/example/components/SideMenuPanel.java
package composants;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.List;
import utils.*;

public class SideMenuPanel extends Panel 
{

    private final WebMarkupContainer menuContainer;
    private final WebMarkupContainer menuIcon;

    public SideMenuPanel(String id, List<ModelLink> menuItems) 
    {
        super(id);

        // Icone du menu pour afficher/masquer le menu
        menuIcon = new WebMarkupContainer("menuIcon");
        menuIcon.setOutputMarkupId(true);
        add(menuIcon);

        // Conteneur pour le menu latéral
        menuContainer = new WebMarkupContainer("menuContainer");
        menuContainer.setOutputMarkupId(true);
        menuContainer.setOutputMarkupPlaceholderTag(true);
        add(menuContainer);

        // RepeatingView pour les éléments du menu
        RepeatingView menuItemsView = new RepeatingView("menuItems");
        menuContainer.add(menuItemsView);

        // Dynamique : Ajoute les éléments du menu avec BookmarkablePageLink
        for (ModelLink item : menuItems) 
        {
            BookmarkablePageLink<Void> menuItemLink = new BookmarkablePageLink<>(menuItemsView.newChildId(), item.getPageClass());
            menuItemLink.add(new Label("itemLabel", item.getName()));
            menuItemsView.add(menuItemLink);
        }

        // Bascule d'affichage/masquage avec AjaxLink
        menuIcon.add(new AjaxLink<Void>("toggleMenu") 
        {
            @Override
            public void onClick(AjaxRequestTarget target) 
            {
                target.appendJavaScript("document.getElementById('" + menuContainer.getMarkupId() + "').classList.toggle('visible');");
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(SideMenuPanel.class, "../css/SideMenuPanel.css")));
    }
}
