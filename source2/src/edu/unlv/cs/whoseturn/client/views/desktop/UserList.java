package edu.unlv.cs.whoseturn.client.views.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;
import com.google.gwt.user.client.ui.Button;

/**
 * Displays a list of all users in the database.
 */
public class UserList extends AbstractNavigationView implements NavigationView {

    /**
     * The user service.
     */
    private final UsersServiceAsync usersService = GWT
            .create(UsersService.class);
    
    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public final Widget bodyAsWidget() {
        // The body of the view.
        final AbsolutePanel panel = new AbsolutePanel();
        panel.setSize("1000px", "500px");

        Label labelTitle = new Label();
        labelTitle.setStyleName("SectionHeader");
        labelTitle.setText("Users");
        panel.add(labelTitle);

        final ListBox guestListBox = new ListBox();
        panel.add(guestListBox, 10, 40);
        guestListBox.setSize("206px", "450px");
        guestListBox.setVisibleItemCount(5);
        
        // Add User
        final Button btnAddUser = new Button("Add User");
        btnAddUser.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                RootPanel.get("overall").clear();
                RootPanel.get("overall").add((new UserAdd()).asWidget());
            }
        });

        // Shows add user button iff user is an admin.
        usersService.isAdmin(new AsyncCallback<Boolean>() {
            public void onFailure(final Throwable caught) {
                System.err.println("Error in isAdmin() when trying to check add button.");
                System.err.println(caught.getStackTrace());
            }

            public void onSuccess(final Boolean isAdmin) {
                if(isAdmin){
                    panel.add(btnAddUser, 256, 106);
                    btnAddUser.setSize("162px", "28px");
                }
            }
        });
        
        // Edit User
        final Button btnEditUser = new Button("Edit User");
        btnEditUser.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                RootPanel.get("overall").clear();
                UserEdit userEdit = new UserEdit();

                // Check to make sure a record is selected before trying to redirect.
                if(guestListBox.getSelectedIndex() > -1){
                    userEdit.setUsername(guestListBox.getItemText(guestListBox.getSelectedIndex()));
                    RootPanel.get("overall").add(userEdit.asWidget());
                }
            }
        });
        
        // Shows edit user button iff user is an admin.
        usersService.isAdmin(new AsyncCallback<Boolean>() {
            public void onFailure(final Throwable caught) {
                System.err.println("Error in isAdmin() when trying to check edit button.");
                System.err.println(caught.getStackTrace());
            }

            public void onSuccess(final Boolean isAdmin) {
                if(isAdmin){
                    panel.add(btnEditUser, 256, 149);
                    btnEditUser.setSize("162px", "28px");
                }
            }
        });
        
        // Display User Profile
        Button btnUserProfile = new Button("Display User Profile");
        btnUserProfile.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                RootPanel.get("overall").clear();
                UserProfileDisplay userProfileDisplay = new UserProfileDisplay();
                
                // Check to make sure a record is selected before trying to redirect.
                if(guestListBox.getSelectedIndex() > -1){
                    userProfileDisplay.setUsername(guestListBox.getItemText(guestListBox.getSelectedIndex()));
                    RootPanel.get("overall").add(userProfileDisplay.asWidget());
                    
                }
            }
        });
        panel.add(btnUserProfile, 256, 196);
        btnUserProfile.setSize("162px", "28px");

        // Display list depends on if it is an admin or not. An admin see's deleted users, where a normal users does not.
        usersService.isAdmin(new AsyncCallback<Boolean>() {
            public void onFailure(final Throwable caught) {
                System.err.println("Error in isAdmin() when trying to check edit button.");
                System.err.println(caught.getStackTrace());
            }

            public void onSuccess(final Boolean isAdmin) {
                if(isAdmin){
                    // Admin sees deleted and non-deleted users.
                    usersService.findAllUsers(new AsyncCallback<List<String>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            System.err.println(caught.getStackTrace());
                        }

                        @Override
                        public void onSuccess(final List<String> results) {
                            if (results != null) {
                                // Write all user names to the list.
                                for (int i = 0; i < results.size(); i++) {
                                    guestListBox.addItem(results.get(i));
                                }
                                guestListBox.setItemSelected(0, true);
                            }
                        }
                    });
//                  usersService.getAllUsers(new AsyncCallback<List<edu.unlv.cs.whoseturn.shared.User>>() {
//
//                      @Override
//                      public void onFailure(Throwable caught) {
//                          System.err.println(caught.getStackTrace());
//                      }
//
//                      @Override
//                      public void onSuccess(final List<edu.unlv.cs.whoseturn.shared.User> results) {
//                          if (results != null) {
//                              // Write all user names to the list.
//                              for (int i = 0; i < results.size(); i++) {
//                                  guestListBox.addItem(results.get(i).getUsername());
//                              }
//                              guestListBox.setItemSelected(0, true);
//                          }
//                      }
//                  });
                } else {
                    // Users see only non-deleted users.
                    usersService.findNonDeletedUsers(new AsyncCallback<List<String>>() {
                        public void onFailure(final Throwable caught) {
                            System.err.println("Error in User List when trying to get user list");
                            System.err.println(caught.getStackTrace());
                        }

                        public void onSuccess(final List<String> results) {
                            if (results != null) {
                                // Write all user names to the list.
                                for (int i = 0; i < results.size(); i++) {
                                    guestListBox.addItem(results.get(i));
                                }
                                guestListBox.setItemSelected(0, true);
                            }
                        }
                    });
                }
            }
        });

        return panel;
    }
}
