package edu.unlv.cs.whoseturn.client.views.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.CategoryService;
import edu.unlv.cs.whoseturn.client.CategoryServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Lists all categories in the database.
 */
public class CategoryList extends AbstractNavigationView implements
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
        AbsolutePanel categoryListPanel = new AbsolutePanel();
        categoryListPanel.setSize("1000px", "500px");

        Label lblTitle = new Label();
        lblTitle.setStyleName("SectionHeader");
        lblTitle.setText("Category List");
        categoryListPanel.add(lblTitle);

        final ListBox categoryListBox = new ListBox();
        categoryListPanel.add(categoryListBox, 10, 40);
        categoryListBox.setSize("381px", "450px");
        categoryListBox.setVisibleItemCount(5);

        categoryService.getAllCategories(new AsyncCallback<List<String>>() {
            public void onFailure(final Throwable caught) {
                // TODO
            }

            public void onSuccess(final List<String> results) {
                if (results != null) {
                    for (int i = 0; i < results.size(); i++) {
                        categoryListBox.addItem(results.get(i));
                    }
                }
            }
        });

        return categoryListPanel;
    }
}
