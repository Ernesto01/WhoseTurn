package edu.unlv.cs.whoseturn.client;

import java.util.List;

import edu.unlv.cs.whoseturn.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WhoseturnMobile implements EntryPoint {
	private final UsersServiceAsync usersService = GWT.create(UsersService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("overall");
		rootPanel.setSize("100%", "100%");
		
	
		StackPanel stackPanel = new StackPanel();
		rootPanel.add(stackPanel, 0, 0);
		stackPanel.setSize("100%", "100%");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		stackPanel.add(absolutePanel, "Confirmations", false);
		absolutePanel.setSize("100%", "100%");
		
		MenuBar menuBar = new MenuBar(false);
		absolutePanel.add(menuBar, 10, 10);
		
		MenuItem mntmOsijdpgoisjdg = new MenuItem("OSIJDPGOISJDG", false, (Command) null);
		menuBar.addItem(mntmOsijdpgoisjdg);
		MenuBar menuBar_1 = new MenuBar(true);
		
		MenuItem mntmSdgsdgsdg = new MenuItem("SDGSDGSDG", false, menuBar_1);
		
		MenuItem mntmJosidjgposidg = new MenuItem("JOSIDJGPOSIDG", false, (Command) null);
		menuBar_1.addItem(mntmJosidjgposidg);
		menuBar.addItem(mntmSdgsdgsdg);
		
		AbsolutePanel absolutePanel_1 = new AbsolutePanel();
		stackPanel.add(absolutePanel_1, "Main", false);
		absolutePanel_1.setSize("100%", "100%");
		
		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		absolutePanel_1.add(decoratedTabPanel, 0, 0);
		decoratedTabPanel.setSize("100%", "100%");
		
		AbsolutePanel absolutePanel_2 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_2, "Main", false);
		absolutePanel_2.setSize("100%", "100%");
		
		Label lblMain = new Label("main");
		absolutePanel_2.add(lblMain, 10, 10);
		
		AbsolutePanel absolutePanel_3 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_3, "Turn", false);
		absolutePanel_3.setSize("100%", "100%");
		
		Label lblTurn = new Label("turn");
		absolutePanel_3.add(lblTurn);
		
		AbsolutePanel absolutePanel_4 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_4, "User Mgt", false);
		absolutePanel_4.setSize("100%", "100%");
		
		Label lblUserMgt = new Label("user mgt");
		absolutePanel_4.add(lblUserMgt);
		
		AbsolutePanel absolutePanel_5 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_5, "Badges", false);
		absolutePanel_5.setSize("100%", "100%");
		
		Label lblBadges = new Label("badges");
		absolutePanel_5.add(lblBadges);
		
		AbsolutePanel absolutePanel_6 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_6, "Credits", false);
		absolutePanel_6.setSize("100%", "100%");
		
		Label lblCredits = new Label("credits");
		absolutePanel_6.add(lblCredits);
		
		AbsolutePanel absolutePanel_7 = new AbsolutePanel();
		stackPanel.add(absolutePanel_7, "Login", false);
		absolutePanel_7.setSize("100%", "100%");
		
		AbsolutePanel absolutePanel_8 = new AbsolutePanel();
		stackPanel.add(absolutePanel_8, "LOOOOOOOOL", false);
		absolutePanel_8.setSize("100%", "100%");
		

		
		/*usersService.isLoggedIn(
				new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						lblNewLabel.setText("FAILURE");
					}

					public void onSuccess(Boolean result) {
						if(result == true)
						{
							usersService.getUsername(
									new AsyncCallback<String>() {
										public void onFailure(Throwable caught) {
											lblNewLabel.setText("FAILURE");
										}
	
										public void onSuccess(String result) {
											lblNewLabel.setText("Welcome "+result);
										}
									});
						}
						else
						{
							usersService.getLoginURL(Window.Location.getPath(),
									new AsyncCallback<String>() {
										public void onFailure(Throwable caught) {
											lblNewLabel.setText("FAILURE");
										}

										public void onSuccess(String result) {
											Window.open(result, "_self", "");
										}
									});
						}
						
					}
				});*/
		

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
	}
}
