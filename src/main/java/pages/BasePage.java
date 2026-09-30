package pages;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import composants.FooterPanel;
import composants.HeaderPanel;
import composants.SideMenuPanel;
import utils.ModelLink;

public class BasePage extends WebPage 
{

    public BasePage() {}

    public BasePage(PageParameters parameters)
    {
        super(parameters);
    }

    public void setHeader()
    {
        // Définition des liens pour le header
        List<ModelLink> headerLinks = List.of(
            new ModelLink(Model.of("Home"), HomePage.class),
            new ModelLink(Model.of("Devis"), DevisPage.class),
            new ModelLink(Model.of("Contact"), ContactPage.class)
        );

        // Ajout du HeaderPanel
        HeaderPanel headerPanel = new HeaderPanel("headerPanel", Model.of("Solar"), headerLinks);
        add(headerPanel);
    }

    public void setMenu()
    {
        // Définition des éléments de menu pour le SideMenuPanel
        List<ModelLink> menuItems = List.of(
            // new ModelLink(Model.of("Dashboard"), HomePage.class),
            // new ModelLink(Model.of("Profile"), HomePage.class),
            // new ModelLink(Model.of("Settings"), HomePage.class)
        );

        // Ajout du SideMenuPanel
        SideMenuPanel sideMenuPanel = new SideMenuPanel("sideMenuPanel", menuItems);
        add(sideMenuPanel);
    }

    public void setFooter()
    {
        // Définition des éléments de menu pour le SideMenuPanel
        List<ModelLink> footerLinks = List.of(
            new ModelLink(Model.of("Help"), HomePage.class),
            new ModelLink(Model.of("Contact"), ContactPage.class)
        );

        // Ajout du footerPanel
        FooterPanel footer = new FooterPanel("footer", footerLinks);
        add(footer);
    }
}
