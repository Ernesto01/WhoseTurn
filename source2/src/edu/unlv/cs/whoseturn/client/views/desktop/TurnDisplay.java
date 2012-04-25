package edu.unlv.cs.whoseturn.client.views.desktop;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * Displays the details of a turn.
 */
public class TurnDisplay extends AbstractNavigationView implements
        NavigationView {

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {
        // The body of the view.

        AbsolutePanel panel = new AbsolutePanel();
        panel.setSize("1000px", "500px");

        Label labelPlaceHolder = new Label();
        panel.add(labelPlaceHolder);
        labelPlaceHolder.setText("Turn Display");

        ToggleButton tglbtnUpText = new ToggleButton("Toggle Button");
        panel.add(tglbtnUpText, 65, 51);

        return panel;
    }
}
