package it.dagu.rover.utils;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class Messages implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private final String COCKPIT_LOCKED = "Cockpit locked by another user, try later or unlock with Admin user.";
	private final String USR_PWD_INVALID = "User or password not valid.";
	private final String USR_LOGOUT = "User logged out.";
	private final String ADMIN_UNLOCK_YOUR_COCKPIT = "Admin forced unlock your cockpit.";
	private final String ENGINES_OFF_TIMEOUT = "Timeout for no response, probably signal lost.";

	public void sendMessage(String txt) {
		customMessage(txt);
	}

	public void cockpitIsLocked() {
		sendMessage(COCKPIT_LOCKED);
	}

	public void userPasswordNotValid() {
		sendMessage(USR_PWD_INVALID);
	}

	public void userLoggedOut() {
		sendMessage(USR_LOGOUT);
	}

	public void adminForcedUnlock() {
		sendMessage(ADMIN_UNLOCK_YOUR_COCKPIT);
	}

	public void enginesOffTimeout() {
		sendMessage(ENGINES_OFF_TIMEOUT);
	}

	private void customMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("", new FacesMessage("", msg));
	}
}
