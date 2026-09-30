package pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.markup.html.form.TextField;

import composants.Utilisation;
import models.Devis;
import models.MaterielDevis;
import utils.Jdbc;

public class HomePage extends BasePage {

    private List<Utilisation> utilisations = new ArrayList<>(); // Liste pour stocker les entrées
    public HomePage() {

        setHeader();        
        setMenu();
        setFooter();
        

        // Ajouter une première utilisation par défaut
        utilisations.add(new Utilisation());

        // Formulaire principal
        Form<?> form = new Form<>("form");
        add(form);

        // ListView pour afficher chaque groupe de champs de saisie
        ListView<Utilisation> listView = new ListView<>("utilisationList", new PropertyModel<>(this, "utilisations")) {
            @Override
            protected void populateItem(ListItem<Utilisation> item) {
                Utilisation utilisation = item.getModelObject();

                item.add(new TextField<>("materiel", new PropertyModel<>(utilisation, "materiel")));
                item.add(new TextField<>("puissance", new PropertyModel<>(utilisation, "puissance")));
                item.add(new TextField<>("heureDebut", new PropertyModel<>(utilisation, "heureDebut")));
                item.add(new TextField<>("heureFin", new PropertyModel<>(utilisation, "heureFin")));
            }
        };
        listView.setReuseItems(true);
        form.add(listView);

        // Bouton pour ajouter une nouvelle entrée
        form.add(new AjaxButton("addButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                utilisations.add(new Utilisation());
                target.add(form);
            }
        });

        // Bouton pour supprimer la dernière entrée
        form.add(new AjaxButton("removeButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (utilisations.size() > 1) {
                    utilisations.remove(utilisations.size() - 1);
                    target.add(form);
                }
            }
        });

        // Bouton pour soumettre le formulaire
        form.add(new Button("submitButton") {
            @Override
            public void onSubmit() {

                Jdbc jdbc = new Jdbc();

                Devis devis = new Devis("N");
                int id = devis.insert(jdbc, "devis");

                utilisations.forEach(utilisation -> {
                    MaterielDevis mat = new MaterielDevis(id, utilisation.getMateriel(), utilisation.getPuissance(), utilisation.getHeureDebut(), utilisation.getHeureFin());
                    int idMat = mat.insert(jdbc, "listeMateriauxDevis");
                });

            }
        });

    }
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(HomePage.class, "../css/pages/HomePage.css")));
    }
}
