package edu.unlv.cs.whoseturn.client.views.desktop;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.CategoryService;
import edu.unlv.cs.whoseturn.client.CategoryServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IntegerBox;

/**
 * Used to allow an admin to add categories to the database.
 */
public class CategoryAdd extends AbstractNavigationView implements
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
        AbsolutePanel categoryAddPanel = new AbsolutePanel();
        categoryAddPanel.setSize("1000px", "500px");

        Label lblName = new Label("Name:");
        categoryAddPanel.add(lblName, 117, 48);

        Label lblTitle = new Label();
        lblTitle.setStyleName("SectionHeader");
        categoryAddPanel.add(lblTitle);
        lblTitle.setText("Category Add");

        Label lblStrategy = new Label("Strategy:");
        categoryAddPanel.add(lblStrategy, 96, 80);
        lblStrategy.setSize("59px", "16px");

        final TextBox txtbxName = new TextBox();
        categoryAddPanel.add(txtbxName, 161, 48);
        txtbxName.setSize("165px", "15px");

        final Button btnAdd = new Button("Add");
        categoryAddPanel.add(btnAdd, 159, 142);

        final ListBox cmbobxStrategy = new ListBox();
        categoryAddPanel.add(cmbobxStrategy, 161, 81);
        cmbobxStrategy.setSize("175px", "22px");

        Label lblTimeBoundary = new Label("Time Boundary(In Hours):");
        categoryAddPanel.add(lblTimeBoundary, 10, 109);
        lblTimeBoundary.setSize("149px", "16px");

        final Label lblSuccessfullyAddedCategory = new Label(
                "Successfully added category");
        categoryAddPanel.add(lblSuccessfullyAddedCategory, 204, 142);
        lblSuccessfullyAddedCategory.setVisible(false);

        final Label lblErrorLabel = new Label("");
        lblErrorLabel.setStyleName("serverResponseLabelError");
        categoryAddPanel.add(lblErrorLabel, 204, 142);

        final IntegerBox timeBoundaryInteger = new IntegerBox();
        categoryAddPanel.add(timeBoundaryInteger, 161, 109);
        timeBoundaryInteger.setSize("167px", "16px");

        categoryService.getAllStrategies(new AsyncCallback<List<String>>() {
            public void onFailure(final Throwable caught) {
                // TODO
            }

            public void onSuccess(final List<String> results) {
                if (results != null) {
                    for (int i = 0; i < results.size(); i++) {
                        cmbobxStrategy.addItem(results.get(i));
                    }
                }
            }
        });

        btnAdd.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                lblErrorLabel.setText("");
                lblSuccessfullyAddedCategory.setVisible(false);
                btnAdd.setEnabled(false);
                categoryService.addCategory(txtbxName.getText(), cmbobxStrategy
                        .getValue(cmbobxStrategy.getSelectedIndex()),
                        timeBoundaryInteger.getValue(),
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                btnAdd.setEnabled(true);
                            }

                            public void onSuccess(final String result) {
                                btnAdd.setEnabled(true);
                                if (result != "Success") {
                                    txtbxName.setFocus(true);
                                    setStatus(result);
                                    lblErrorLabel.setText(result);
                                } else {
                                    txtbxName.setText("");
                                    lblSuccessfullyAddedCategory
                                            .setVisible(true);
                                }
                            }
                        });
            }
        });

        return categoryAddPanel;
    }
}
