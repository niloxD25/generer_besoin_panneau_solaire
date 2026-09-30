package pages;

import composants.TableComponent;
import models.Devis;
import utils.Gestion;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.List;

public class DevisPage extends BasePage 
{
    public DevisPage() 
    {
        setHeader();
        setMenu();

        // Ajouter la liste des devis table à la page
        Gestion ge = new Gestion();
        List<Devis> devis = ge.getObjectsFromTable("vue_devis_confirmation", Devis.class, null);
        add(new TableComponent<>("devisTable", Model.ofList(devis), List.of("estConfirme", "adresse", "nom", "id")));

        setFooter();
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(DevisPage.class, "../css/pages/DevisPage.css")));
    }
}
