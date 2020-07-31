package it.dagu.rover.controller;

import it.dagu.rover.model.Configuration;
import it.dagu.rover.model.User;
import it.dagu.rover.model.UserLogged;
import it.dagu.rover.utils.Cockpit;
import it.dagu.rover.utils.Messages;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String SUCCESS_PAGE = "dagu?faces-redirect=true";
	private final String UNSUCCESS_PAGE = "login?faces-redirect=false";
	private final String LOGOUT_PAGE = "login?faces-redirect=false";
	private String user;

	@Inject
	private Configuration configuration;
	@Inject
	private Cockpit cockpit;
	@Inject
	private Messages messages;
	@Inject
	private UserLogged userLogged;

	public LoginController() {
	}

	public String getUser() {
		return user;
	}

	private String password;

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
		User user = (User) configuration.getUsers().get(getUser());
		if (user != null) {
			userLogged.setAuthorized(Boolean.valueOf(false));
			String name = user.getName();
			String pwd = user.getPassword();
			if ((name != null) && (pwd != null) && (name.equals(getUser())) && (pwd.equals(getPassword()))) {
				userLogged.setName(name);
				userLogged.setPassword(pwd);
				if ((cockpit.activateLock()) || ("admin".equals(name))) {
					userLogged.setAuthorized(Boolean.valueOf(true));
					return SUCCESS_PAGE;
				}
				messages.cockpitIsLocked();
				return UNSUCCESS_PAGE;
			}
		}

		messages.userPasswordNotValid();
		return "login?faces-redirect=false";
	}

	public String logout() {
		userLogged.setAuthorized(Boolean.valueOf(false));
		userLogged.setName("");
		userLogged.setPassword("");
		setUser("");
		setPassword("");
		cockpit.removeLock();
		messages.userLoggedOut();
		return LOGOUT_PAGE;
	}
}
