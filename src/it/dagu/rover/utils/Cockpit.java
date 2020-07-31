package it.dagu.rover.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import it.dagu.rover.model.UserLogged;

@Named
@SessionScoped
public class Cockpit implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private File lock;
	private final String PREFIX = "/tmp/";
	private final String POSTFIX = "_daguRover.lck";

	@Inject
	private UserLogged userLogged;
	
	public boolean isLockActive() {
		if ((lock != null) && (anyLockExist())) {
			return true;
		}
		return false;
	}

	public boolean isLockActiveForLoggedUser() {
		if ((isLockActive()) && (lockExistsForLoggedUser())) {
			return true;
		}
		return false;
	}

	public boolean activateLock() {
		try {
			lock = new File(PREFIX + userLogged.getName() + POSTFIX);
			if ((isLockActive()) && (!"admin".equals(userLogged.getName()))) {
				System.out.println("unable to created cockpit lock : " + lock.getAbsolutePath());
				return false;
			}
			lock.createNewFile();
			System.out.println("created cockpit lock : " + lock.getAbsolutePath());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeLock() {
		if (isLockActive()) {
			lock.delete();
			System.out.println("deleted cockpit lock : " + lock.getAbsolutePath());
			return true;
		}
		return false;
	}

	public void anyLockRemove() {
		File dir = new File(PREFIX);
		for (int i = 0; i < dir.listFiles().length; i++) {
			File f = dir.listFiles()[i];
			if (f.getName().endsWith(".lck")) {
				f.delete();
			}
		}
	}

	public boolean anyLockExist() {
		File dir = new File(PREFIX);
		for (int i = 0; i < dir.listFiles().length; i++) {
			File f = dir.listFiles()[i];
			if (f.getName().endsWith(".lck")) {
				return true;
			}
		}
		return false;
	}

	public boolean lockExistsForLoggedUser() {
		File dir = new File(PREFIX);
		for (int i = 0; i < dir.listFiles().length; i++) {
			File f = dir.listFiles()[i];
			if (f.getName().contains(userLogged.getName())) {
				return true;
			}
		}
		return false;
	}
}
