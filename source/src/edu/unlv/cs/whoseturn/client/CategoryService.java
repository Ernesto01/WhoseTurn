package edu.unlv.cs.whoseturn.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("categories")
public interface CategoryService extends RemoteService {
	public void addCategory(String category);
	public void removeCategory(String category);
	public List<String> getAllCategories();
	public void loadInitialCategories();
}
