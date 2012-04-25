package edu.unlv.cs.whoseturn.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import edu.unlv.cs.whoseturn.client.views.desktop.Login;
import edu.unlv.cs.whoseturn.client.views.desktop.Main;
import edu.unlv.cs.whoseturn.client.views.desktop.UserNotFound;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Whoseturn implements EntryPoint {
    /**
     * The user service.
     */
    private final UsersServiceAsync usersService = GWT
            .create(UsersService.class);
    
    /**
     * This is the entry point method.
     */
    public final void onModuleLoad() {
        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        /**
         * The rootPanel of the display, the root panel of our entry point.
         */
        final RootPanel rootPanel = RootPanel.get("overall");
        rootPanel.setSize("1000px", "750px");

        usersService.isLoggedIn(new AsyncCallback<Boolean>() {
            public void onFailure(final Throwable caught) {
                // TODO Add error message
            }

            public void onSuccess(final Boolean result) {
                if (result) {
                    usersService.getUsername(new AsyncCallback<String>() {
                        public void onFailure(final Throwable caught) {
                            // TODO Add error message
                        }

                        public void onSuccess(final String result) {
                            if (result.equals("UserNotFound")) {
                                RootPanel.get("overall").clear();
                                RootPanel.get("overall").add(
                                        (new UserNotFound()).asWidget());
                            } else {
                                RootPanel.get("overall").clear();
                                RootPanel.get("overall").add(
                                        (new Main()).asWidget());
                            }
                        }

                    });
                } else {
                    RootPanel.get("overall").clear();
                    RootPanel.get("overall").add((new Login()).asWidget());
                }
            }
        });
    }
}
