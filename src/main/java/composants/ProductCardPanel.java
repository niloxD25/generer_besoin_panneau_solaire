package composants;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import pages.HomePage;
import models.Product;

public class ProductCardPanel extends Panel 
{
    
    public ProductCardPanel(String id, IModel<Product> productModel) 
    {
        super(id, productModel);
        
        Product product = productModel.getObject();
        
        // Image du produit
        ResourceReference imageResource = new PackageResourceReference(getClass(), product.getImagePath());
        Image productImage = new Image("productImage", imageResource);
        productImage.setOutputMarkupId(true);
        add(productImage);

        // Nom du produit
        add(new Label("productName", Model.of(product.getName())));

        // Prix du produit
        add(new Label("productPrice", Model.of("$" + product.getPrice())));

        // Lien vers les détails du produit
        BookmarkablePageLink<Void> detailsLink = new BookmarkablePageLink<>("detailsLink", HomePage.class);
        add(detailsLink);
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(ProductCardPanel.class, "../css/ProductCardPanel.css")));
    }
}
