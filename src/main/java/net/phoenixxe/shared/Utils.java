package net.phoenixxe.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.JavaScriptException;

import net.phoenixxe.shared.enums.Constants;
import net.phoenixxe.shared.model.HasIdAndTitle;
import net.phoenixxe.shared.persistence.PersistedEntity;
import net.phoenixxe.shared.persistence.Realm;

/**
 * Basic utils.
 * @author Arseniy Kaleshnyk, ver. 1.0
 */
public class Utils {
	/**
	 * Checks that object is empty.
	 * @param object - checked object
	 * @return true if object is null
	 */
	public static boolean isEmpty(Object object) {
		return (object == null); 
	}
	
	/**
	 * Checks that String is null or empty.
	 * @param string - checked String
	 * @return true if string is null or empty
	 */
	public static boolean isEmpty(String string) {
		return isStringEmpty(string);
	}
	
	/**
	 * Checks that String is null or empty.
	 * @param string - checked String
	 * @return true if string is null or empty
	 */
	public static boolean isStringEmpty(String string) {
		return (string == null) || (string.trim().isEmpty());
	}
	
	/**
	 * Checks that Long is null or 0l.
	 * @param longObject - checked Long
	 * @return true if Long is null or 0l
	 */
	public static boolean isEmpty(Long longObject) {
		return isLongEmpty(longObject); 		 
	}
	
	/**
	 * Checks that Long is null or 0l.
	 * @param longObject - checked Long
	 * @return true if Long is null or 0l
	 */
	public static boolean isLongEmpty(Long longObject) {
		// TODO: check GWT using, may be <= 0 is allowed here
		return (longObject == null) || (longObject.longValue() == 0);
	}
	
	/**
	 * Checks that List is null or empty.
	 * @param list - checked List
	 * @return false if List is null or empty
	 */
	public static boolean isEmpty(List<?> list) {
		return isListEmpty(list);
	}
	
	/**
	 * Checks that List is null or empty.
	 * @param list - checked List
	 * @return false if List is null or empty
	 */
	public static boolean isListEmpty(List<?> list) {
		return (list == null) || (list.isEmpty());
	}
	
	public static boolean isEmpty(HasIdAndTitle titledItem) {
		return isItemEmpty(titledItem);
	}
	
	/**
	 * Checks that specified HasIdAndTitle entity is null or has empty <b>id</b> or <b>title</b> fields.<br>
	 * Entities backed by DB, but not persisted at the moment, can have null <b>id</b>,<br>
	 * so <b>id</b> is checked only to be non-zero.<br>
	 * @param titledItem - HasIdAndTitle entity to be checked
	 * @return true if HasIdAndTitle entity is null or has empty id or title fields.
	 */
	public static boolean isItemEmpty(HasIdAndTitle titledItem) {
		return (titledItem == null)
				|| (titledItem.getId() <= 0) 
				|| (isStringEmpty(titledItem.getTitle()));
	}
	
	/**
	 * Trims specified string and converts to low register. 
	 * @param value - string to be normalized
	 * @return normalized string or empty string in case specified string was null
	 */
	public static String normaliseString(String value) {
		if (value == null) return "";
		return value.trim().toLowerCase();
	}
	
	// TODO: javadoc and tests
	public static String formatTitle(String value) {
		if (value == null) return "";
		return value.trim();
	}
	
	public static boolean isIds(Object object) {
		return (object != null) &&
				(object instanceof ArrayList<?>) &&
				(((ArrayList<?>) object).size() > 0) &&
				(((ArrayList<?>) object).get(0) instanceof Long);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<HasIdAndTitle> convertArray(ArrayList<?> entities) {
		if (entities == null) return null;
		return (ArrayList<HasIdAndTitle>) (ArrayList<?>) entities;
	}
	
	public static String formatArray(ArrayList<HasIdAndTitle> titledEntities) {
		if (titledEntities == null) return "N/A";
		if (titledEntities.isEmpty()) return "None";
		String titles="";
		for (HasIdAndTitle entity : titledEntities) {
			if (entity == null) continue;
			if (!titles.isEmpty()) titles = titles + "\n";
			titles = titles + entity.getTitle();
		}
		return titles;
	}
	
	@SuppressWarnings("unchecked")
	public static String formatEntitiesArray(ArrayList<PersistedEntity> titledEntities) {
		return formatArray((ArrayList<HasIdAndTitle>) (ArrayList<?>) titledEntities);
	}
	
	public static boolean isSaas(Realm realm) {
		if(realm == null) return false;
		return Constants.SAAS_REALM.getTitle().equals(realm.getTitle());
	}
	
	protected static final String NULL_EXCEPTION_STRING = "null exception";
	protected static final String DECODE_EXCEPTION_STRING = " CAUSE / MESSAGE ";
	public static String decodeException(final Exception e) {
		if (e == null) {
			return NULL_EXCEPTION_STRING;
		} else if (e instanceof JavaScriptException) {
			String message = e.getMessage();
			return DECODE_EXCEPTION_STRING.
			replaceFirst("CAUSE", "JavaScriptException").
			replaceFirst("MESSAGE", message.substring(
					0,
					(message.length() > 22 ? 22 : message.length())));
		} else {
			return DECODE_EXCEPTION_STRING.
					replaceFirst("CAUSE", e.getClass().getName()).
					replaceFirst("MESSAGE", e.getMessage());
		}
	}
	
	protected static final String NULL_THROWABLE_STRING = "null throwable";
	public static String decodeThrowable(final Throwable t) {
		if (t == null) {
			return NULL_THROWABLE_STRING;
		} else if (t instanceof JavaScriptException) {
			return decodeException((JavaScriptException) t);
		} else {
			return DECODE_EXCEPTION_STRING.
					replaceFirst("CAUSE", t.getClass().getName()).
					replaceFirst("MESSAGE", t.getMessage());
		}
	}
	
	/**
	 * 
	 * @param domain
	 * @return
	 */
	public static boolean isNonHostedDomain(String domain) {
		domain = normaliseString(domain);
		if (domain.isEmpty()) return false;
		return (domain.equals("google.com") 
				|| domain.equals("gmail.com"));
	}
	
	// #216
	public static boolean areCollectionsEqual(
			Collection<?> collection1, 
			Collection<?> collection2) {
		if (collection1 == collection2) return true;
		
		if (collection1 == null || collection2 == null) return false;

		if (collection1.size() != collection2.size()) {
			return false;
		}
		
		if (collection1.isEmpty()) return true;
		
		int nulls1 = 0;
		int nulls2 = 0;
		for (Object itemA : collection1) {
			if (itemA == null) {
				nulls1++;
				continue;
			}
			boolean matched = false;
			for (Object itemB : collection2) {
				if (itemB == null) {
					nulls2++;
					continue;
				}
				
				if (itemA.equals(itemB)) {
					matched = true;
					break;
				}
			}
			
			if (!matched) return false;
		}
		
		if (nulls1 != nulls2) return false;
		// TODO: due to using clone() containsAll isn't usable here 
		//if (collection1.containsAll(collection2) && collection2.containsAll(collection1)) return true;
		return true;
	}
}
