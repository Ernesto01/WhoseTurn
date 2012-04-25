package edu.unlv.cs.whoseturn.client.views.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.CategoryService;
import edu.unlv.cs.whoseturn.client.CategoryServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;

/**
 * Allows an admin to edit categories in the database.
 */
public class CategoryEdit extends AbstractNavigationView implements
        NavigationView {

    /**
     * The category service.
     */
    private final CategoryServiceAsync categoryService = GWT
            .create(CategoryService.class);
    
    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {
        // The body of the view.
        FlowPanel panel = new FlowPanel();
        panel.setSize("1000px", "500px");

        Label labelPlaceHolder = new Label();
        labelPlaceHolder.setText("Category Edit");
        panel.add(labelPlaceHolder);

        return panel;
    }

}
