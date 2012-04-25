package edu.unlv.cs.whoseturn.client.views.desktop;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.unlv.cs.whoseturn.client.UsersService;
import edu.unlv.cs.whoseturn.client.UsersServiceAsync;
import edu.unlv.cs.whoseturn.client.views.AbstractNavigationView;
import edu.unlv.cs.whoseturn.client.views.NavigationView;

import com.google.gwt.user.client.ui.TextBox;

/**
 * POC that changes to view2 when the button is clicked.
 */
public class UserEdit extends AbstractNavigationView implements NavigationView {

	/**
	 * The user service.
	 */
	private final UsersServiceAsync usersService = GWT
			.create(UsersService.class);

	/**
	 * The user name of a user we'll be editing.
	 */
	private String userName;

	/**
	 * Contains the information about the user to populate the form with.
	 * 
	 * A list of zero or one that has a string[] with the information.
	 * {username, email, admin, deleted}
	 */
	private List<String[]> userInfo;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public final Widget bodyAsWidget() {

		// The body of the view.
		final AbsolutePanel panel = new AbsolutePanel();
		panel.setSize("1000px", "500px");

		Label lblLbltitle = new Label("User Edit");
		lblLbltitle.setStyleName("SectionHeader");
		panel.add(lblLbltitle);

		Label lblUsername = new Label("UserName:");
		panel.add(lblUsername, 17, 50);
		final TextBox txtbxUsername = new TextBox();
		panel.add(txtbxUsername, 98, 45);
		txtbxUsername.setSize("161px", "16px");

		Label lblEmail = new Label("Email:");
		panel.add(lblEmail, 45, 87);
		final TextBox txtbxEmail = new TextBox();
		panel.add(txtbxEmail, 98, 80);
		txtbxEmail.setSize("161px", "20px");

		Label lblAdmin = new Label("Admin:");
		panel.add(lblAdmin, 41, 118);
		final CheckBox chckbxAdmin = new CheckBox("");
		panel.add(chckbxAdmin, 98, 117);

		Label lblDeleted = new Label("Deleted:");
		panel.add(lblDeleted, 34, 143);
		final CheckBox chkDeleted = new CheckBox("");
		panel.add(chkDeleted, 98, 143);
		chkDeleted.setSize("20px", "19px");

		final Label lblError = new Label("");
		lblError.setStyleName("serverResponseLabelError");
		panel.add(lblError, 161, 192);

		Button btnSave = new Button("Save");
		panel.add(btnSave, 98, 187);

		final Label lblSuccess = new Label("Successfully updated user.");
		panel.add(lblSuccess, 161, 192);
		lblSuccess.setVisible(false);

		// Get the user info and populate it to the form.
		usersService.getUserInfo(userName, new AsyncCallback<List<String[]>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println(caught.getStackTrace());
			}

			@Override
			public void onSuccess(List<String[]> result) {
				userInfo = result;

				// Set username.
				txtbxUsername.setText(userInfo.get(0)[0]);

				// Set email address.
				txtbxEmail.setText(userInfo.get(0)[1]);

				// Set admin check box.
				if (userInfo.get(0)[2].toLowerCase().equals("true")) {
					chckbxAdmin.setValue(true);
				} else {
					chckbxAdmin.setValue(false);
				}

				// Set deleted check box.
				if (userInfo.get(0)[3].toLowerCase().equals("true")) {
					chkDeleted.setValue(true);
				} else {
					chkDeleted.setValue(false);
				}
			}
		});

		btnSave.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				lblError.setText("");
				lblSuccess.setVisible(false);

				usersService.updateUser(userName, userInfo.get(0)[1], txtbxUsername.getText(),
						txtbxEmail.getText(), chckbxAdmin.getValue(),
						chkDeleted.getValue(), new AsyncCallback<String>() {
							public void onFailure(final Throwable caught) {
								lblError.setText("There was an unknown error.");
							}

							public void onSuccess(final String result) {
								if (!result.equals("Success")) {
									lblError.setText(result);
								} else {
									lblSuccess.setVisible(true);
								}
							}
						});
			}
		});

		return panel;
	}

	/**
	 * Set the user name, so we know which record to edit.
	 * 
	 * @param userName
	 *            The username.
	 */
	public void setUsername(String userName) {
		this.userName = userName;
	}
}
