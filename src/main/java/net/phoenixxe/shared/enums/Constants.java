package net.phoenixxe.shared.enums;

import java.util.ArrayList;

public enum Constants implements EnumInterface<Constants> {
	SAAS_REALM("Saas"),
	NONE("None");
	
	private String title;

	private Constants(String title) {
		this.title = title;
	}

	@Override
	public Constants defaultValue() {
		return NONE;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public static ArrayList<Constants> allValues() {
		ArrayList<Constants> list = new ArrayList<Constants>();
		for (Constants entity : Constants.values()) list.add(entity);
		return list;
	}

	public String toString() {
		return "#" + this.ordinal() + "/" + title;
	}

	@Override
	public Long getId() {
		return Long.valueOf(ordinal());
	}

	@Override
	public void setId(Long id) {}
}
