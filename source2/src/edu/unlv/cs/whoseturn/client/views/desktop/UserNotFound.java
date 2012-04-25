package edu.unlv.cs.whoseturn.client.views.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;
import edu.unlv.cs.whoseturn.client.views.View;

/**
 * POC that changes to view2 when the button is clicked.
 */
public class UserNotFound implements View {

    /**
     * The user service.
     */
    private final UsersServiceAsync usersService = GWT
            .create(UsersService.class);

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget asWidget() {
        String url = GWT.getHostPageBaseURL();
        if (!GWT.isProdMode()) {
            url += "?gwt.codesvr=127.0.0.1:9997";
        }
        final String finalURL = url;

        final AbsolutePanel userNotFoundPanel = new AbsolutePanel();
        userNotFoundPanel.setSize("1000px", "500px");

        Label lblNotloggedin = new Label(
                "This e-mail address was not found in our database. Please contact an administrator to be invited.");
        userNotFoundPanel.add(lblNotloggedin);

        Button btnUserNotFoundLogout = new Button("Logout");
        btnUserNotFoundLogout.setText("Return to Login");
        userNotFoundPanel.add(btnUserNotFoundLogout);

        btnUserNotFoundLogout.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLogoutURL(finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                //
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        return userNotFoundPanel;
    }
}
