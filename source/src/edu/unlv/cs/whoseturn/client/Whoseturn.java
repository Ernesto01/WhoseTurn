package edu.unlv.cs.whoseturn.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
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
import com.google.gwt.widget.client.TextButton;

import edu.unlv.cs.whoseturn.domain.Category;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Whoseturn implements EntryPoint {
	private final UsersServiceAsync usersService = GWT.create(UsersService.class);
	
	// UI widget variables
	RootPanel rootPanel = null;
	final Label lblNewLabel = new Label("Loading logged in user...");
	final AbsolutePanel loginPanel = new AbsolutePanel();
	TextBox txtbxEnterCategory = new TextBox();
	Label lblCategoryStatus = new Label("Category status");
	ListBox listBox = new ListBox(false);
	private FlexTable userFlexTable = new FlexTable();
	Label lblUserlabel = new Label("userLabel");
	TextButton txtbtnChooseUser = new TextButton("Choose User");
	
	// Functionality variables
	private List<String> users = new ArrayList<String>();
	private ArrayList<String> selectedUsers = new ArrayList<String>();
	private String selectedCategory;
	private List<String> categoryNames = new ArrayList<String>();
	
	// Declare and define services
	private final CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
	

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
		
		final Label lblNewLabel_1 = new Label("Choose Category");
		absolutePanel_1.add(lblNewLabel_1, 10, 10);
		
		// Get all category names and use them to populate combo box
		loadCategories();
		
		
		// Handle Category input here --------
		txtbxEnterCategory.setText("Enter category");
		absolutePanel_1.add(txtbxEnterCategory, 175, 35);
		txtbxEnterCategory.setSize("158px", "20px");
		
		// Listen for keyboard events in the input box.
		txtbxEnterCategory.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          addCategory();
	        }
	      }
	    });
		
		TextButton txtbtnAddCategory = new TextButton("Add Category");
		absolutePanel_1.add(txtbtnAddCategory, 347, 35);
		txtbtnAddCategory.setSize("102px", "28px");
		
		// Listen for mouse events on the Add button.
	    txtbtnAddCategory.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        addCategory();
	      }
	    });
		// ------------
		
		absolutePanel_1.add(lblCategoryStatus, 175, 69);
		lblCategoryStatus.setSize("114px", "28px");
		
		
		absolutePanel_1.add(listBox, 10, 34);
		listBox.setSize("96px", "68px");
		listBox.setVisibleItemCount(5);
		
		// Add User table and configure -------
		userFlexTable.setText(0, 0, "Username");
	    userFlexTable.setText(0, 1, "Select");
	    userFlexTable.setText(0, 2, "Unselect");
	    userFlexTable.setText(0, 3, "Remove");
		absolutePanel_1.add(userFlexTable, 10, 114);
		
		
		absolutePanel_1.add(lblUserlabel, 347, 144);
		lblUserlabel.setSize("98px", "28px");
		
		
		absolutePanel_1.add(txtbtnChooseUser, 347, 110);
		txtbtnChooseUser.setSize("102px", "28px");
		
		loadUsers();
		addUsersToTable();
	  
	    
	    // -----------
		
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
	
	// Add category from the category text box
	private void addCategory() {
	    final String symbol = txtbxEnterCategory.getText().toUpperCase().trim();
	    txtbxEnterCategory.setFocus(true);

	    // Category must be between 1 and 30 chars that are numbers, letters, or dots.
	    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,30}$")) {
	      Window.alert("'" + symbol + "' is not a valid category.");
	      txtbxEnterCategory.selectAll();
	      return;
	    }

	    txtbxEnterCategory.setText("");

	    // Don't add the category if it's already in the table.
	    if (categoryNames.contains(symbol))
	      return;
	     

	    addToComboBox(symbol);
	    addCategory(symbol);
	}
	
	// Load users from datastore to memory for UI output
	private void loadUsers() {
		usersService.getUsernames(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable error) {
				lblUserlabel.setText("failure");
			}
			
			public void onSuccess(List<String> result) {
				if(!result.isEmpty())
					lblUserlabel.setText("not empty");
				addUsersToTable(result);

			}
		});
	}
	
	//Populate categories list with categories in datastore
	private void loadCategories() {
		categoryService.getAllCategories(new AsyncCallback<List<String>>() {
			public void onFailure(Throwable error) {
				
			}
			
			public void onSuccess(List<String> result) {
				addCategoriesToList(result);
			}
		});
	}
	
	private void addCategoriesToList(List<String> categories) {
		for(String category : categories) {
			addToComboBox(category);
		}
	}
	
	private void addUsersToTable() {
		int row = 0;
		for(String user : users) {
			row = userFlexTable.getRowCount();
			userFlexTable.setText(row, 0, user);
		}
	}
	
	private void addUsersToTable(final List<String> users) {
		int row = 0;
	
		for(final String user : users) {
			row = userFlexTable.getRowCount();
			userFlexTable.setText(row, 0, user);
			
			// Add second column to remove user
			Button addUserButton = new Button("Select");
			addUserButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int addIndex = users.indexOf(user);
					selectedUsers.add(user);
				}
			});
			
			// Add third column to deselect user
			Button unselectUserButton = new Button("Unselect");
			addUserButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectedUsers.remove(user);
				}
			});
			
			// Add fourth column to remove user
			Button removeUserButton = new Button("x");
		    removeUserButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = users.indexOf(user);
		        userFlexTable.removeRow(removedIndex + 1);
		      }
		    });
		    userFlexTable.setWidget(row, 2, addUserButton);
		    userFlexTable.setWidget(row, 3, unselectUserButton);
		    userFlexTable.setWidget(row, 4, removeUserButton);
		}
		
		
	}
	
	private void addCategory(String newCategory) {
		
		categoryService.addCategory(newCategory, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				lblCategoryStatus.setText("Error");
			}
			public void onSuccess(Void ignore) {
				lblCategoryStatus.setText("Category Added");
			}
		});
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
	
	private void addToComboBox(String category) {
		categoryNames.add(category);
		listBox.addItem(category);
	}
}
