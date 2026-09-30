package composants;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.resource.CssResourceReference;

import utils.*;

public class FooterPanel extends Panel 
{

    public FooterPanel(String id, List<ModelLink> links) 
    {
        super(id);

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
        response.render(CssHeaderItem.forReference(new CssResourceReference(FooterPanel.class, "../css/FooterPanel.css")));
    }
}
