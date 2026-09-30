package composants;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import pages.DetailsPage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TableComponent<T> extends Panel 
{

    private final List<String> fieldsToDisplay;

    public TableComponent(String id, IModel<List<T>> itemsModel) 
    {
        this(id, itemsModel, null);
    }

    public TableComponent(String id, IModel<List<T>> itemsModel, List<String> columnNames) 
    {
        super(id, itemsModel);

        List<T> items = itemsModel.getObject();
        Label noData = new Label("noData", "No data available");
        noData.setVisible(false);
        add(noData);

        if (items == null || items.isEmpty()) 
        {
            fieldsToDisplay = Collections.emptyList();
            noData.setVisible(true);
            add(new Label("headers", "").setVisible(false));
            add(new Label("header", "").setVisible(false));
            add(new Label("rows", "").setVisible(false));
            add(new Label("columns", "").setVisible(false));
            add(new Label("column", "").setVisible(false));
            return;
        }

        T firstItem = items.get(0);
        List<String> allFields = getAllFieldNames(firstItem.getClass());

        fieldsToDisplay = (columnNames != null && !columnNames.isEmpty())
                ? allFields.stream().filter(columnNames::contains).collect(Collectors.toList())
                : allFields;

        ListView<String> headers = new ListView<>("headers", fieldsToDisplay) 
        {
            @Override
            protected void populateItem(ListItem<String> item) 
            {
                item.add(new Label("header", item.getModelObject()));
            }
        };
        add(headers);

        ListView<T> rows = new ListView<>("rows", itemsModel) 
        {
            @Override
            protected void populateItem(ListItem<T> item) 
            {
                T rowObject = item.getModelObject();
                ListView<String> columns = new ListView<>("columns", fieldsToDisplay) {
                    @Override
                    protected void populateItem(ListItem<String> columnItem) {
                        String fieldName = columnItem.getModelObject();
                        Object value = getFieldValue(rowObject, fieldName);
                
                        if ("id".equals(fieldName)) {
                            BookmarkablePageLink<Void> columnLink = new BookmarkablePageLink<>("columnLink", DetailsPage.class);
                            columnLink.getPageParameters().add("id", value != null ? value.toString() : "N/A");
                            columnLink.add(new Label("column", value != null ? value.toString() : "N/A"));
                            columnItem.add(columnLink);
                            columnItem.add(new EmptyPanel("column").setVisible(false));
                        } else {
                            columnItem.add(new EmptyPanel("columnLink").setVisible(false));
                            columnItem.add(new Label("column", value != null ? value.toString() : "N/A"));
                        }
                    }
                };

                item.add(columns);
            }
        };
        add(rows);
    }

    private static List<String> getAllFieldNames(Class<?> clazz) 
    {
        List<String> fieldNames = new ArrayList<>();
        while (clazz != null) 
        {
            for (Field field : clazz.getDeclaredFields()) 
            {
                fieldNames.add(field.getName());
            }
            clazz = clazz.getSuperclass();
        }
        return fieldNames;
    }

    private Object getFieldValue(T object, String fieldName) 
    {
        Class<?> clazz = object.getClass();
        while (clazz != null) 
        {
            try 
            {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException | IllegalAccessException e) 
            {
                clazz = clazz.getSuperclass();
            }
        }
        return "N/A";
    }

    @Override
    public void renderHead(IHeaderResponse response) 
    {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(TableComponent.class, "../css/TableComponent.css")));
    }
}
