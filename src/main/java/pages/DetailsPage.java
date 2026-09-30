package pages;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import composants.TableComponent;
import models.Batterie;
import models.Devis;
import models.MaterielDevis;

import org.apache.wicket.model.Model;

public class DetailsPage extends BasePage
{
    public DetailsPage(PageParameters parameters) 
    {
        super(parameters);
        String id = parameters.get("id").toString();

        int idDevis = Integer.parseInt(id);

        setTableauListeMateriauxDevis(idDevis);

        setHeader();
        setMenu();
        setFooter();
    }

    public void setTableauListeMateriauxDevis(int idDevis)
    {
        try 
        {
            Devis devis = new Devis(idDevis);
            devis.setListeMateriauxDevisFromBase();
            List<MaterielDevis> listeMateriauxDevis = devis.getListeMateriauxDevis();

            add(new TableComponent<>("listeMateriauxTable", Model.ofList(listeMateriauxDevis), List.of("materiel", "puissance", "heureDebutUtilisation", "heureFinUtilisation", "utilisationMatin", "utilisationSoir", "consommationMatin", "consommationSoir")));
            
            devis.calculTotalConso();

            Double prixPanneau = devis.calculPrixPanneau();
            Double puissancepanneau = devis.calculPuissancePanneau();
            Double prixParW = devis.getPrixParWPanneau();

            Double reste = devis.getReste();
            Double prixReste = reste * 20;

            add(new Label("consoJournee", puissancepanneau / 2));
            add(new Label("puissancePanneau", puissancepanneau));
            add(new Label("prixPanneau", prixPanneau));
            add(new Label("prixParW", prixParW));

            add(new Label("reste", reste));
            add(new Label("prixRest", prixReste));

            add(new Label("consoSoiree", devis.getConsoTotalSoiree()));

            List<Batterie> listeBatteries = devis.calculPuissancePrixParBatterie();

            add(new TableComponent<>("listeBatterie", Model.ofList(listeBatteries), List.of("name", "desce", "vraiCapacite", "prixParW", "capaciteAFournir", "prixTotal")));

            add(new Label("total1", (prixPanneau + listeBatteries.get(0).getPrixTotal()) + " Ar si " + listeBatteries.get(0).getName()));
            add(new Label("total2", (prixPanneau + listeBatteries.get(1).getPrixTotal()) + " Ar si " + listeBatteries.get(1).getName()));
            add(new Label("total3", (prixPanneau + listeBatteries.get(2).getPrixTotal()) + " Ar si " + listeBatteries.get(2).getName()));

            // Ajouter le bouton "Confirmer" si le devis n'est pas confirmé
            if("N".equals(devis.getEstConfirme()) || devis.getEstConfirme() == null) 
            {
                PageParameters params = new PageParameters();
                params.add("idDevis", idDevis);

                add(new BookmarkablePageLink<Void>("confirmerButton", ConfirmationPage.class, params).add(new Label("confirmerLabel", "Confirmer")));
            }

        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } 

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(DetailsPage.class, "../css/pages/DetailsPage.css")));
    }
}
