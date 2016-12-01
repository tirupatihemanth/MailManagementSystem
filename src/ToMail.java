import java.io.Serializable;

/**
 * A single mail can have many recipients hence can have many ToMail classes
 * with each recepients address
 * 
 * @author HemanthKumarTirupati
 *
 */
public class ToMail implements MailTree, Serializable {

	private static final long serialVersionUID = -8465116787318420577L;
	private String toAddress;
	private String fromAddress;
	private String subject;
	private String date;
	private long UID;
	private boolean valid;

	/**
	 * Here toAddress must be parsed before sending All the others can be passed
	 * directly with the data obtained from the database
	 * 
	 * @param toAddress
	 * @param valid
	 * @param UID
	 * @param fromAddress
	 * @param subject
	 * @param date
	 */
	public ToMail(String toAddress, String valid, String UID,
			String fromAddress, String subject, String date) {
		this.toAddress = toAddress;
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

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

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

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		ToMail toMail = (ToMail) o;
		return this.toAddress.compareTo(toMail.toAddress);
	}

	@Override
	public int compareWith(MailTree mail) {
		// TODO Auto-generated method stub
		ToMail toMail = (ToMail) mail;
		int len2 = toMail.toAddress.length();
		if (this.toAddress.length() >= toMail.getToAddress().length()) {
			return this.toAddress.substring(0, len2).compareTo(
					toMail.getToAddress());
		} else {
			len2 = toMail.getToAddress().length();
			return this.toAddress.compareTo(toMail.getToAddress().substring(0,
					len2));
		}
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

}
