package edu.unlv.cs.whoseturn.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Whoseturn implements EntryPoint {
	private final UsersServiceAsync usersService = GWT.create(UsersService.class);
	
	// UI widget variables
	RootPanel rootPanel = null;
	final Label lblNewLabel = new Label("Loading logged in user...");
	final AbsolutePanel loginPanel = new AbsolutePanel();
	
	// Functionality variables
	private ArrayList<String> selectedUsers = new ArrayList<String>();
	private String selectedCategory;
	
	// Declare and define services
	private final CategoryServiceAsync stockService = GWT.create(CategoryService.class);
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		String url = GWT.getHostPageBaseURL();
		if(!GWT.isProdMode()) {
			url += "?gwt.codesvr=127.0.0.1:9997";
		}
		final String finalURL = url;
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		rootPanel = RootPanel.get("overall");
		
		// Set size to auto adjust based on widgets inside
		rootPanel.setSize("auto", "auto");
		
		// Log in users
		handleLogIn(finalURL);
		
		
		
		final DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		rootPanel.add(decoratedTabPanel);
		decoratedTabPanel.setSize("664px", "714px");
		decoratedTabPanel.setVisible(false);
		
		final AbsolutePanel absolutePanel_1 = new AbsolutePanel();
		absolutePanel_1.setVisible(false);
		decoratedTabPanel.add(absolutePanel_1, "Main", false);
		absolutePanel_1.setSize("650px", "544px");
		
		Label lblNewLabel_1 = new Label("Choose Category");
		absolutePanel_1.add(lblNewLabel_1, 10, 10);
		
		ListBox comboBox = new ListBox();
		absolutePanel_1.add(comboBox, 10, 35);
		comboBox.setSize("96px", "22px");
		
		// Table to hold and display users for selection
		CellTable<Object> cellTable = new CellTable<Object>();
		absolutePanel_1.add(cellTable, 10, 74);
		cellTable.setSize("189px", "156px");
		
		final AbsolutePanel absolutePanel_2 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_2, "AdminSettings", false);
		absolutePanel_2.setSize("649px", "540px");
		
		final AbsolutePanel absolutePanel_3 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_3, "UserSettings", false);
		absolutePanel_3.setSize("652px", "543px");
		
		Label lblListUsers = new Label("List Users");
		absolutePanel_3.add(lblListUsers, 38, 68);
		
		Button btnAddUser = new Button("Add User");
		absolutePanel_3.add(btnAddUser, 26, 138);
		
		Button btnAddGuest = new Button("Add Guest");
		absolutePanel_3.add(btnAddGuest, 116, 138);
		
		Button btnLogout = new Button("Logout");
		absolutePanel_3.add(btnLogout, 10, 24);
		
		btnLogout.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLogoutURL(finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
		
		final AbsolutePanel absolutePanel_4 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_4, "User Profile", false);
		absolutePanel_4.setSize("651px", "543px");
		
		final AbsolutePanel absolutePanel_5 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_5, "Turn History", false);
		absolutePanel_5.setSize("650px", "544px");
		
		final AbsolutePanel absolutePanel_6 = new AbsolutePanel();
		decoratedTabPanel.add(absolutePanel_6, "Testing", false);
		absolutePanel_6.setSize("644px", "471px");
		
		final Label lblEmail = new Label("Email:");
		absolutePanel_6.add(lblEmail, 36, 69);
		
		final Label lblKeystring = new Label("");
		absolutePanel_6.add(lblKeystring, 178, 186);
		lblKeystring.setSize("53px", "18px");
		Button btnNewButton = new Button("Query Users");
		absolutePanel_6.add(btnNewButton, 10, 431);
		btnNewButton.setSize("91px", "30px");
		
		btnNewButton.setText("Query User");
		

		final TextArea textArea = new TextArea();
		absolutePanel_6.add(textArea, 10, 210);
		textArea.setVisibleLines(10);
		textArea.setSize("336px", "205px");
		
		Button btnAdduser = new Button("adduser");
		absolutePanel_6.add(btnAdduser, 269, 91);
		
		btnAdduser.setText("Add User");
		
		final Label lblCreated = new Label("Created user with keystring:");
		absolutePanel_6.add(lblCreated, 10, 186);
		
		final CheckBox chckbxAdmin = new CheckBox("");
		absolutePanel_6.add(chckbxAdmin, 81, 149);
		
		Label lblAdmin = new Label("Admin:");
		absolutePanel_6.add(lblAdmin, 34, 149);
		
		final TextBox txtbxEmail = new TextBox();
		absolutePanel_6.add(txtbxEmail, 79, 69);
		
		final Label lblUsername = new Label("Username:");
		absolutePanel_6.add(lblUsername, 10, 109);
		
				
		final TextBox txtbxUsername = new TextBox();
		absolutePanel_6.add(txtbxUsername, 79, 109);
		lblCreated.setVisible(false);
		
		btnAdduser.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.addTestUser(txtbxUsername.getText(), txtbxEmail.getText(), chckbxAdmin.getValue(),
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblCreated.setText("FAILURE");
							}

							public void onSuccess(String result) {
								lblCreated.setVisible(true);
								lblKeystring.setText(result);
							}
						});
			}
		});
		
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.findUsers(
						new AsyncCallback<List<String[]>>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(List<String[]> result) {
								textArea.setText("");
								for (String[] row : result)
								{
									textArea.setText(textArea.getText()+"Username: "+row[0]+"\nEmail: "+row[1]+"\nAdmin: "+row[2]+"\n\n");
								}
								
							}
						});
			}
		});
		
		
		
		usersService.isLoggedIn(
				new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						lblNewLabel.setText("FAILURE");
					}
	
					public void onSuccess(Boolean result) {
						if(result == true)
						{
							decoratedTabPanel.setVisible(true);
							usersService.getUsername(
									new AsyncCallback<String>() {
										public void onFailure(Throwable caught) {
											lblNewLabel.setText("FAILURE");
										}
	
										public void onSuccess(String result) {
											lblNewLabel.setText(result);
										}
											
									});
						}
						else
						{
							loginPanel.setVisible(true);
						}
					}
		});
		
		// Select initial tab 
		decoratedTabPanel.selectTab(0);
	}
	
	// Handle log in of user
	private void handleLogIn(final String finalURL) {
		rootPanel.add(loginPanel);
		loginPanel.setSize("455px", "100px");
		loginPanel.setVisible(false);
		loginPanel.add(lblNewLabel);
		
		Image googleImage = new Image("images/googleW.png");
		loginPanel.add(googleImage);
		
		Image yahooImage = new Image("images/yahooW.png");
		loginPanel.add(yahooImage);
		
		Image aolImage = new Image("images/aolW.png");
		loginPanel.add(aolImage);
		
		Image myspaceImage = new Image("images/myspaceW.png");
		loginPanel.add(myspaceImage);
		
		Image openIDImage = new Image("images/myopenidW.png");
		loginPanel.add(openIDImage);
		
		googleImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLoginURL("google", finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
		

		yahooImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLoginURL("yahoo", finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
		

		aolImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLoginURL("aol", finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
		

		myspaceImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLoginURL("myspace", finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
		

		openIDImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				usersService.getLoginURL("myopenid", finalURL,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								lblNewLabel.setText("FAILURE");
							}

							public void onSuccess(String result) {
								Window.open(result, "_self", "");
							}
						});
			}
		});
	}
	
}
