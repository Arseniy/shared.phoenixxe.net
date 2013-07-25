package net.phoenixxe.shared.enums;

import net.phoenixxe.shared.model.HasIdAndTitle;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface EnumInterface<E extends Enum<E>> 
		extends java.io.Serializable, IsSerializable, HasIdAndTitle {
	int ordinal();
	E defaultValue();
}
