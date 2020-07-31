package it.dagu.rover.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Engines implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean leftForward;
	private boolean rightForward;
	private boolean allForward;
	private boolean engine;
	private int left = 0;
	private int right = 0;

	public boolean isLeftForward() {
		return leftForward;
	}

	public void setLeftForward(boolean leftForward) {
		this.leftForward = leftForward;
	}

	public boolean isRightForward() {
		return rightForward;
	}

	public void setRightForward(boolean rightForward) {
		this.rightForward = rightForward;
	}

	public boolean isAllForward() {
		return allForward;
	}

	public void setAllForward(boolean allForward) {
		this.allForward = allForward;
	}

	public boolean isEngine() {
		return engine;
	}

	public void setEngine(boolean engine) {
		this.engine = engine;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}
}
