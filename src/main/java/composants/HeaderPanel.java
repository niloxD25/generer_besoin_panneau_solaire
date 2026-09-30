package composants;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import java.util.List;
import utils.*;

public class HeaderPanel extends Panel 
{
    public HeaderPanel(String id, IModel<String> logoText, List<ModelLink> links) 
    {
        super(id);

        // Logo avec texte
        add(new Label("logo", logoText));

        // Boucle pour ajouter chaque lien dans le header
        RepeatingView linksView = new RepeatingView("links");
        for (ModelLink link : links) 
        {
            BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<>(linksView.newChildId(), link.getPageClass());
            pageLink.add(new Label("linkLabel", link.getName()));
            linksView.add(pageLink);
        }
        add(linksView);
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(HeaderPanel.class, "../css/HeaderPanel.css")));
    }
}
