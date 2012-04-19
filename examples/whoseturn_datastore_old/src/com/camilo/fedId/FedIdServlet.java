/*
 * This is the sign-in screen.
 * 
 */

package com.camilo.fedId;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class FedIdServlet extends HttpServlet {

    private static final Map<String, String> openIdProviders;
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("google", "www.google.com/accounts/o8/id");
        openIdProviders.put("yahoo", "yahoo.com");
        openIdProviders.put("myspace", "myspace.com");
        openIdProviders.put("aol", "aol.com");
        openIdProviders.put("myopenid", "myopenid.com");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser(); // or req.getUserPrincipal()
        Set<String> attributes = new HashSet();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (user != null) {
        	resp.sendRedirect("/main");
            out.println("Hello <i>" + user.getNickname() + "</i>!");
            out.println("[<a href=\""
                    + userService.createLogoutURL(req.getRequestURI())
                    + "\">sign out</a>]");
        } else {
            out.println("<p><b>Hello! Sign in at: </b></p>");
            for (String providerName : openIdProviders.keySet()) {
                String providerUrl = openIdProviders.get(providerName);
                String loginUrl = userService.createLoginURL(req
                        .getRequestURI(), null, providerUrl, attributes);
                out.println("<a href=\"" + loginUrl + "\"> <img src=\"images/" + providerName + "W.png\" " + 
                        "width=\"95\" height=\"27\" style=\"border: 0px;\" alt=\"Link Description\" /></a> ");
            }
        }
    }
}