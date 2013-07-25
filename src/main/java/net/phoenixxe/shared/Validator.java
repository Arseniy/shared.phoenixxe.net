package net.phoenixxe.shared;

import net.phoenixxe.shared.model.HasIdAndTitle;
import net.phoenixxe.shared.persistence.PersistedEntity;

/**
 * Validator.
 * 
 * @author Arseniy Kaleshnyk, ver. 1.0
 */
public class Validator {
	// --------------------------- Basic validation ---------------------------
	/**
	 * Checks that specified object is not-null.
	 * 
	 * @param value
	 *            - validated Object
	 * @param title
	 *            - used by customize exception message
	 * @throws IllegalArgumentException
	 *             if object is null
	 */
	public static void checkObject(Object value, String title)
			throws IllegalArgumentException {
		if (value == null)
			throw new IllegalArgumentException(title + " is NULL");
	}

	/**
	 * Check that specified object is not-null. Calls
	 * {@link Validator#checkObject(Object, String)}
	 * 
	 * @param value
	 *            - validated Object
	 * @throws IllegalArgumentException
	 *             if object is null
	 */
	public static void checkObject(Object value)
			throws IllegalArgumentException {
		checkObject(value, "object");
	}

	/**
	 * Check that specified Id is not-null and positive.
	 * 
	 * @param id
	 *            - validated Id
	 * @param title
	 *            - used by customize exception message
	 * @throws IllegalArgumentException
	 *             if Id is null or non-positive
	 */
	public static void checkId(Long id, String title)
			throws IllegalArgumentException {
		checkObject(id, title);
		if (id.longValue() <= 0l)
			throw new IllegalArgumentException(new StringBuilder("invalid ")
					.append(title).append(" = ").append(id).toString());
	}

	/**
	 * Check that specified Id is not-null and positive.
	 * 
	 * @param id
	 *            - checked Id
	 * @throws IllegalArgumentException
	 *             if Id is null or non-positive
	 */
	public static void checkId(Long id) throws IllegalArgumentException {
		checkId(id, "id");
	}

	/**
	 * Check that String exist and not empty
	 * 
	 * @param value
	 *            - checked value
	 * @param title
	 *            - used by exception throw
	 * @throws IllegalArgumentException
	 *             if empty or null
	 */
	public static void checkString(String value, String title)
			throws IllegalArgumentException {
		checkObject(value, title);
		if (value.trim().isEmpty())
			throw new IllegalArgumentException(new StringBuilder("invalid ")
					.append(title).append(" = ").append(value).toString());
	}

	/**
	 * Check that entity exist and has id and title
	 * 
	 * @param entity
	 *            - checked value
	 * @param title
	 *            - used by exception throw
	 * @throws IllegalArgumentException
	 *             if other
	 */
	public static void checkItem(HasIdAndTitle entity, String title)
			throws IllegalArgumentException {
		checkObject(entity, title);
		if (entity.getId() != null)
			checkId(entity.getId(), title + ".id");
		checkString(entity.getTitle(), title + ".title");
	}

	/**
	 * Check that entity exist and has id and title
	 * 
	 * @param entity
	 * @throws IllegalArgumentException
	 */
	public static void checkItem(HasIdAndTitle entity)
			throws IllegalArgumentException {
		// checkObject(entity, "entity");
		checkItem(entity, "entity");
	}

	/**
	 * Check that entity is persisted. In case non persisted set realmId for
	 * entity.
	 * 
	 * @param entity
	 * @param title
	 * @param realmId
	 * @throws IllegalArgumentException
	 */
	public static void checkPersistedEntity(PersistedEntity entity,
			String title, long realmId) throws IllegalArgumentException {
		checkItem(entity, title);

		if (realmId == 0l)
			return;
		// non-persisted
		if (entity.getId() == null) {
			if (entity.getRealmId() != null)
				throw new IllegalArgumentException(
						"non-empty realm for non-persistent " + title + " : "
								+ entity.toString());
			entity.setRealmId(Long.valueOf(realmId));
			return;
		}

		// persisted
		checkId(entity.getRealmId(), "realmId");
		if (entity.getRealmId().longValue() != realmId)
			throw new IllegalArgumentException(new StringBuilder(
					"realm doesn't match = ")
					.append(entity.getRealmId().longValue())
					.append(" instead of ").append(realmId).toString());
	}

	// --------------------- Specific validation
	// -----------------------------------

	/**
	 * Regular expression pattern for email validation, reflects RFC2822
	 */
	private final static String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
			+ "@"
			+ "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	/**
	 * Validates email address.
	 * 
	 * @param email
	 *            - email address, should satisfy RFC2822 (Internet Message
	 *            Format);
	 * @throws IllegalArgumentException
	 *             if email is null or invalid
	 */
	public static void validateEmail(String email)
			throws IllegalArgumentException {
		checkString(email, "email");
		String[] splitArr = Utils.normaliseString(email).split(emailPattern);
		if (splitArr.length != 0)
			throw new IllegalArgumentException("invalid email : " + email);

	}

	/**
	 * Validates email and its accessory to domain.
	 * 
	 * @param email
	 *            - email address, should satisfy RFC2822 (Internet Message
	 *            Format);
	 * @param domain
	 *            - domain, should satisfy RFC2822 (Internet Message Format);
	 * @throws IllegalArgumentException
	 *             if email is null or invalid, or domain is null or invalid,<br>
	 *             or email doesn't belong to domain.
	 */
	public static void validateEmail(String email, String domain)
			throws IllegalArgumentException {
		validateEmail(email);
		checkString(domain, "domain");
		domain = Utils.normaliseString(domain);
		email = Utils.normaliseString(email);
		if (domain.compareTo(email.split("@")[1]) != 0)
			throw new IllegalArgumentException("email doesn't belong to domain");
	}

	/**
	 * Validates that the fiscal year is in the format MM/dd
	 * 
	 * @param fiscalYear
	 */
	public static void validateFiscalYearFormat(String fiscalYear) {
		
		String exceptionMessage = "Invalid Fiscal Year Format" ;
		
		if (fiscalYear != null && fiscalYear.matches("[0-9]{1,2}/[0-9]{1,2}")) {
			Integer month = new Integer(fiscalYear.split("/")[0]);
			Integer year = new Integer(fiscalYear.split("/")[1]);
			
			if (month < 1 || month > 12) {
				throw new IllegalArgumentException(exceptionMessage);
			}			
			if (year < 0 || year > 99) {
				throw new IllegalArgumentException(exceptionMessage);
			}
		} else {
			throw new IllegalArgumentException(exceptionMessage);
		}		
		
		
//		if (fiscalYear.length() < 5) {
//			// string is less than 5
//			throw new IllegalArgumentException(exceptionMessage);
//		} else if (fiscalYear.length() == 5) {
//			// string is equal to 5. check for correct month/year values
//			String monthStr = fiscalYear.substring(0, 2);
//			String separatorStr = fiscalYear.substring(2, 3);
//			String yearStr = fiscalYear.substring(3, 5);
//			//check the month
//			if(!isNumeric(monthStr)){
//				//the month is not a number
//				throw new IllegalArgumentException(exceptionMessage);
//			}else {
//				//if its a number, check if its between 01-12
//				Integer month = new Integer(monthStr);
//				if(month < 1){
//					throw new IllegalArgumentException(exceptionMessage);
//				}else if (month > 12){
//					throw new IllegalArgumentException(exceptionMessage);
//				}
//			}
//			
//			//check the year
//			if(!isNumeric(yearStr)){
//				// the year is not a number
//				throw new IllegalArgumentException(exceptionMessage);
//			}else {
//				//its a number check if its between 00-99
//				Integer year = new Integer(yearStr);
//				if(year < 0){
//					throw new IllegalArgumentException(exceptionMessage);
//				}else if(year > 99){
//					throw new IllegalArgumentException(exceptionMessage);
//				}
//			}
//
//		} else {
//			// string is greater than 5
//			throw new IllegalArgumentException(exceptionMessage);
//		}
	}

	public static boolean isNumeric(String str) {
//		NumberFormat formatter = NumberFormat.getInstance();
//		ParsePosition pos = new ParsePosition(0);
//		formatter.parse(str, pos);
//		return str.length() == pos.getIndex();
		
		return str.matches("[0-9]{0,}");
	}

}