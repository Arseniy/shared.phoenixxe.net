package net.phoenixxe.shared.enums;

import java.util.ArrayList;

public enum SimpleChoice implements EnumInterface<SimpleChoice> {
	YES("Yes"),
	NO("No");

	private String title;

	private SimpleChoice(String title) {
		this.title = title;
	}

	@Override
	public SimpleChoice defaultValue() {
		return NO;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public static ArrayList<SimpleChoice> allValues() {
		ArrayList<SimpleChoice> list = new ArrayList<SimpleChoice>();
		for (SimpleChoice entity : SimpleChoice.values()) list.add(entity);
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
