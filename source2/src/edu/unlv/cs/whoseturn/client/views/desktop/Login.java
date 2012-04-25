package edu.unlv.cs.whoseturn.client.views.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;
import edu.unlv.cs.whoseturn.client.views.View;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;

/**
 * View used to allow users to login. We extend the view interface because we do
 * not want the navigation bar to appear until a user is logged in.
 */
public class Login implements View {

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
        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element

        final AbsolutePanel loginPanel = new AbsolutePanel();
        loginPanel.setSize("1000px", "500px");

        final Label lblNewLabel = new Label(
                "Please login choose one of the following options.");
        loginPanel.add(lblNewLabel);

        Image googleImage = new Image("images/googleW.png");
        googleImage.setStyleName("ImageLink");
        googleImage.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {

            }
        });
        loginPanel.add(googleImage);

        Image yahooImage = new Image("images/yahooW.png");
        yahooImage.setStyleName("ImageLink");
        loginPanel.add(yahooImage);

        Image aolImage = new Image("images/aolW.png");
        aolImage.setStyleName("ImageLink");
        loginPanel.add(aolImage);

        Image myspaceImage = new Image("images/myspaceW.png");
        myspaceImage.setStyleName("ImageLink");
        loginPanel.add(myspaceImage);

        Image openIDImage = new Image("images/myopenidW.png");
        openIDImage.setStyleName("ImageLink");
        loginPanel.add(openIDImage);

        final TextBox txtbxEmail = new TextBox();
        loginPanel.add(txtbxEmail, 79, 60);

        final Label lblEmail = new Label("Email:");
        loginPanel.add(lblEmail, 36, 60);

        final Label lblUsername = new Label("Username:");
        loginPanel.add(lblUsername, 10, 100);

        Label lblAdmin = new Label("Admin:");
        loginPanel.add(lblAdmin, 34, 140);

        final CheckBox chckbxAdmin = new CheckBox("");
        loginPanel.add(chckbxAdmin, 81, 140);

        final TextBox txtbxUsername = new TextBox();
        loginPanel.add(txtbxUsername, 79, 100);

        Button btnAdduser = new Button("adduser");
        loginPanel.add(btnAdduser, 269, 82);

        btnAdduser.setText("Add User");

        final Label lblCreated = new Label("Created user with keystring:");
        loginPanel.add(lblCreated, 10, 177);

        final Label lblKeystring = new Label("");
        loginPanel.add(lblKeystring, 178, 177);
        lblKeystring.setSize("53px", "18px");

        Button btnInitalizeDatabase = new Button("Initalize Database");
        btnInitalizeDatabase.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.initializeServer(new AsyncCallback<Void>() {
                    public void onFailure(final Throwable caught) {
                        // TODO
                    }

                    public void onSuccess(final Void result) {
                        lblKeystring.setText("Initialized");
                    }
                });
            }
        });
        loginPanel.add(btnInitalizeDatabase, 80, 247);
        lblCreated.setVisible(false);

        btnAdduser.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                // Checks if the e-mail has a chance to be valid and if it
                // already exists.
                /*
                 * if (!FieldVerifier.isEmailValid(txtbxEmail.getText(),
                 * lblKeystring)) { return; } // Checks to see if the user name
                 * already exists if
                 * (!FieldVerifier.isUsernameValid(txtbxUsername.getText(),
                 * lblKeystring)) { return; }
                 */
                usersService.addNewUser(txtbxUsername.getText(),
                        txtbxEmail.getText(), chckbxAdmin.getValue(),
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblCreated.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                // Result is an error message or the key if
                                // successful
                                lblKeystring.setText(result);
                                lblCreated.setVisible(true);
                                // lblKeystring.setText(result);
                            }
                        });
            }
        });

        googleImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLoginURL("google", finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblNewLabel.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        yahooImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLoginURL("yahoo", finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblNewLabel.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        aolImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLoginURL("aol", finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblNewLabel.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        myspaceImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLoginURL("myspace", finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblNewLabel.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        openIDImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLoginURL("myopenid", finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                lblNewLabel.setText("FAILURE");
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });

        return loginPanel;
    }
}
