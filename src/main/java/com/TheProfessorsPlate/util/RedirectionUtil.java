package com.TheProfessorsPlate.util;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ryan Khan
 */
public class RedirectionUtil {

	/**
	 * Sets an attribute in the request scope
	 */
	public void setMsgAttribute(HttpServletRequest req, String msgType, String msg) {
		req.setAttribute(msgType, msg);
	}
	
	/**
	 * Sets a message in session scope (survives redirects)
	 */
	public void setSessionMsgAttribute(HttpServletRequest req, String msgType, String msg) {
		HttpSession session = req.getSession();
		session.setAttribute(msgType, msg);
	}

	/**
	 * Forwards the request to another page
	 */
	public void redirectToPage(HttpServletRequest req, HttpServletResponse resp, String page)
			throws ServletException, IOException {
		req.getRequestDispatcher(page).forward(req, resp);
	}
	
	/**
	 * Redirects to a URL, doesn't carry request attributes
	 */
	public void sendRedirect(HttpServletResponse resp, String url) throws IOException {
		resp.sendRedirect(url);
	}

	/**
	 * Sets a message attribute in request and forwards to a page
	 */
	public void setMsgAndRedirect(HttpServletRequest req, HttpServletResponse resp, String msgType, String msg,
			String page) throws ServletException, IOException {
		// Store message in session instead of request for proper redirects
		setSessionMsgAttribute(req, msgType, msg);
		// Use sendRedirect instead of forward to avoid recursion
		resp.sendRedirect(req.getContextPath() + page);
	}
}