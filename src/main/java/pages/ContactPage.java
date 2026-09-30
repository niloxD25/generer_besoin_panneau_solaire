// src/main/java/com/example/pages/ContactPage.java
package pages;

import composants.InputPanel;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

public class ContactPage extends BasePage 
{

    public ContactPage() 
    {
        Model<String> nameModel = Model.of("");
        Model<String> emailModel = Model.of("");
        Model<String> messageModel = Model.of("");

        Form<?> contactForm = new Form<>("contactForm");

        InputPanel nameInput = new InputPanel("nameInput", nameModel, "../icons/circle-user-solid.svg", "text");
        contactForm.add(nameInput);

        InputPanel emailInput = new InputPanel("emailInput", emailModel, "../icons/envelope-solid.svg", "email");
        contactForm.add(emailInput);

        InputPanel messageInput = new InputPanel("messageInput", messageModel, null, "text");
        contactForm.add(messageInput);

        Button submitButton = new Button("submitButton") 
        {
            @Override
            public void onSubmit() 
            {
                super.onSubmit();
                System.out.println("Nom: " + nameModel.getObject());
                System.out.println("Email: " + emailModel.getObject());
                System.out.println("Message: " + messageModel.getObject());
            }
        };
        contactForm.add(submitButton);

        setHeader();
        setMenu();
        setFooter();

        add(contactForm);
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(ContactPage.class, "../css/pages/ContactPage.css")));
    }
}
