package edu.unlv.cs.whoseturn.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Used as a temporary placeholder during development process.
 */
public class ViewImpl1 extends AbstractNavigationView implements NavigationView {

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {

        // The body of the view.
        FlowPanel panel = new FlowPanel();
        Button button1 = new Button();
        button1.setText("View1 - Place holder, this will have to be implemented into its own panel.");
        button1.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                System.out.println("view1.button1 clicked handled");
                RootPanel.get("overall").clear();
                RootPanel.get("overall").add((new ViewImpl2()).asWidget());
            }
        });

        panel.add(button1);

        return panel;
    }

}
