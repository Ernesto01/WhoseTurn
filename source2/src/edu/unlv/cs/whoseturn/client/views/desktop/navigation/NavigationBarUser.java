package edu.unlv.cs.whoseturn.client.views.desktop.navigation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.views.View;
import edu.unlv.cs.whoseturn.client.views.ViewImpl2;

/**
 * This is a navigation bar that will be seen by regular users.
 */
public class NavigationBarUser implements View {
    
    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget asWidget() {
        FlowPanel navigationBar = new FlowPanel();
        Button button1 = new Button();
        button1.setText("View1");
        button1.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                System.out.println("view1.button1 clicked handled");
                RootPanel.get("overall").clear();
                RootPanel.get("overall").add((new ViewImpl2()).asWidget());
            }
        });

        navigationBar.add(button1);

        return navigationBar;
    }
}
