package edu.unlv.cs.whoseturn.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CategoryServiceAsync {
	public void addCategory(String category, AsyncCallback<Void> async);
	public void removeCategory(String category, AsyncCallback<Void> async);
	public void getAllCategories(AsyncCallback<List<String>> async);
}
