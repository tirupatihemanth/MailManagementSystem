import java.io.Serializable;

/**
 * A single mail can have many ToNameMail for each word of the name of the
 * recepient
 * 
 * @author HemanthKumarTirupati
 *
 */
public class ToNameMail implements MailTree, Serializable {
	private static final long serialVersionUID = -8465116787318420577L;
	private String subToName;
	private long UID;
	private boolean valid;
	private String fromAddress;
	private String subject;
	private String date;

	/**
	 * subToName is the substring of the name of a recepient
	 * 
	 * @param subToName
	 * @param valid
	 * @param UID
	 * @param fromAddress
	 * @param subject
	 * @param date
	 */
	public ToNameMail(String subToName, String valid, String UID,
			String fromAddress, String subject, String date) {
		this.subToName = subToName.toLowerCase();
		this.fromAddress = Include.parseAddress(fromAddress, 6);
		this.subject = subject;
		this.date = date;
		this.UID = Long.parseLong(UID);
		valid = valid.substring(7);
		if (valid.equals("1")) {
			this.valid = true;
		} else {
			this.valid = false;
		}
	}

	/**
	 * Getters, Setters and override methods
	 */
	public Long getUID() {
		return UID;
	}

	@Override
	public void setUID(Long UID) {
		this.UID = UID;

	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getSubToName() {
		return subToName;
	}

	public void setSubToName(String subToName) {
		this.subToName = subToName;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setUID(long uID) {
		UID = uID;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		ToNameMail toNameMail = (ToNameMail) o;
		return this.subToName.compareTo(toNameMail.subToName);
	}

	@Override
	public int compareWith(MailTree mail) {
		// TODO Auto-generated method stub
		ToNameMail toNameMail = (ToNameMail) mail;
		return this.subToName.compareTo(toNameMail.subToName);
	}

}
