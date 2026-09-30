package composants;

import models.Product;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.List;

public class ProductList extends Panel 
{
    public ProductList(String id, IModel<List<Product>> productsModel) 
    {
        super(id, productsModel);

        List<Product> items = productsModel.getObject();
        Label noData = new Label("noData", "No data available");
        noData.setVisible(false);
        add(noData);

        if (items.isEmpty() || items == null) 
        {
            noData.setVisible(true);
            add(new Label("productListView", "").setVisible(false));
            add(new Label("productCard", "").setVisible(false));
            return;
        }

        // Création d'un ListView pour générer une ProductCard pour chaque produit
        ListView<Product> productListView = new ListView<>("productListView", productsModel) 
        {
            @Override
            protected void populateItem(ListItem<Product> item) 
            {
                item.add(new ProductCardPanel("productCard", item.getModel()));
            }
        };
        add(productListView);
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(ProductList.class, "../css/ProductList.css")));
    }
}
