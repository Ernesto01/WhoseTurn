package edu.unlv.cs.whoseturn.client.views.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;

/**
 * POC that changes to view2 when the button is clicked.
 */
public class Main extends AbstractNavigationView implements NavigationView {

    /**
     * The user service.
     */
    private final UsersServiceAsync usersService = GWT
            .create(UsersService.class);

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {
        final DecoratedTabPanel turnDecoratedTabPanel = new DecoratedTabPanel();
        turnDecoratedTabPanel.setSize("1000px", "500px");

        final AbsolutePanel findDriverPanel = new AbsolutePanel();
        turnDecoratedTabPanel.add(findDriverPanel, "Find Driver", false);
        findDriverPanel.setSize("990px", "450px");

        final AbsolutePanel viewMyTurnHistoryPanel = new AbsolutePanel();
        turnDecoratedTabPanel.add(viewMyTurnHistoryPanel, "My Turn History",
                false);
        viewMyTurnHistoryPanel.setSize("990px", "450px");

        final AbsolutePanel testingPanel = new AbsolutePanel();
        turnDecoratedTabPanel.add(testingPanel, "Testing", false);
        testingPanel.setSize("990px", "450px");

        Button btnNewButton = new Button("Query Users");
        testingPanel.add(btnNewButton, 10, 286);
        btnNewButton.setSize("91px", "30px");

        btnNewButton.setText("Query User");

        final TextArea textArea = new TextArea();
        testingPanel.add(textArea, 10, 65);
        textArea.setVisibleLines(10);
        textArea.setSize("336px", "205px");

        Label lblQueriesTheDatabase = new Label(
                "Queries the database for all users");
        testingPanel.add(lblQueriesTheDatabase, 10, 10);
        turnDecoratedTabPanel.selectTab(0);

        btnNewButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.findUsers(new AsyncCallback<List<String[]>>() {
                    public void onFailure(final Throwable caught) {
                        textArea.setText("FAILURE");
                    }

                    public void onSuccess(final List<String[]> result) {
                        textArea.setText("");
                        for (String[] row : result) {
                            textArea.setText(textArea.getText() + "Username: "
                                    + row[0] + "\nEmail: " + row[1]
                                    + "\nAdmin: " + row[2] + "\n\n");
                        }

                    }
                });
            }
        });

        return turnDecoratedTabPanel;
    }
}
