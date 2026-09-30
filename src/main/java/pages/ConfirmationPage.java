package pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import models.ConfirmationDevis;
import models.Devis;
import utils.Jdbc;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.Button;

public class ConfirmationPage extends BasePage
{
    private String nom;
    private String adresse;
    private Jdbc jdbc = new Jdbc();

    public ConfirmationPage(PageParameters parameters) 
    {
        super(parameters);

        setHeader();
        setMenu();
        setFooter();

        // Récupérer l'ID du devis
        int idDevis = Integer.parseInt(parameters.get("idDevis").toString());

        add(new Label("devisId", "Devis ID: " + idDevis));

        Form<?> form = new Form<Void>("confirmationForm");

        form.add(new TextField<String>("nom", new PropertyModel<>(this, "nom")));
        form.add(new TextField<String>("adresse", new PropertyModel<>(this, "adresse")));

        form.add(new Button("enregistrerButton") 
        {
            @Override
            public void onSubmit() 
            {
                Devis devis = new Devis(idDevis);
                devis.setEstConfirme("Y");
                devis.update(jdbc, "devis");

                ConfirmationDevis coDe = new ConfirmationDevis(idDevis, adresse, nom);
                coDe.insert(jdbc, "confirmationDevis");

                System.out.println("Nom: " + nom);
                System.out.println("Adresse: " + adresse);
            }
        });

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(ConfirmationPage.class, "../css/pages/ConfirmationPage.css")));
    }
}
