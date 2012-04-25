package edu.unlv.cs.whoseturn.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;

/**
 * Status bar give feedback to the admin/user as they use the program.
 */
public class StatusBarImpl implements StatusBar {

    /**
     * String that contains the status to display in the status view.
     */
    private String status = "";
    
    /**
     * The user service.
     */
    private final UsersServiceAsync usersService = GWT
            .create(UsersService.class);

    /**
     * Default Constructor.
     */
    public StatusBarImpl() {
        // status = "";
        status = "Status bar status";
    }

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
        
        // The body of the view.
        FlowPanel panel = new FlowPanel();
        Label statusLabel = new Label();
        statusLabel.setText(status);
        panel.add(statusLabel);
        statusLabel.setWidth("451px");
        
        Button btnLogout = new Button("Logout");
        btnLogout.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                usersService.getLogoutURL(finalURL,
                        new AsyncCallback<String>() {
                            public void onFailure(final Throwable caught) {
                                System.err.println(caught.getStackTrace());
                            }

                            public void onSuccess(final String result) {
                                Window.open(result, "_self", "");
                            }
                        });
            }
        });        
        panel.add(btnLogout);
        
        return panel;
    }

    @Override
    public final String getStatus() {
        return status;
    }

    @Override
    public final void setStatus(final String status) {
        this.status = status;
    }

}
