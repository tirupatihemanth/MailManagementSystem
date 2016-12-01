import java.io.Serializable;

/**
 * From Mail is the mail class used by any node of From Search Tree
 * 
 * @author HemanthKumarTirupati
 */
public class FromMail implements MailTree, Serializable {

	private static final long serialVersionUID = -8465116787318420577L;
	private String fromAddress;
	private String subject;
	private String date;
	private long UID;
	private boolean valid;

	// Date Format: Tue Jan 15 12:13:36 IST 2013

	/**
	 * All the fields to be passed are just raw data
	 * 
	 * @param fromAddress
	 * @param valid
	 * @param UID
	 * @param subject
	 * @param date
	 */

	public FromMail(String fromAddress, String valid, String UID,
			String subject, String date) {
		// System.out.println(UID+" :uid");
		this.subject = subject;
		this.date = date;
		this.fromAddress = Include.parseAddress(fromAddress, 6);
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
	 * 
	 * @return
	 */

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

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Long getUID() {
		return UID;
	}

	@Override
	public void setUID(Long UID) {
		this.UID = UID;
	}

	/**
	 * used to insert a node into a tree
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		FromMail fromMail = (FromMail) o;
		return this.fromAddress.compareTo(fromMail.getFromAddress());

	}

	/**
	 * Used to search in search tree
	 */

	@Override
	public int compareWith(MailTree mail) {
		// TODO Auto-generated method stub
		int len2;
		FromMail fromMail = (FromMail) mail;
		len2 = fromMail.getFromAddress().length();
		if (this.fromAddress.length() >= len2) {
			System.out.println("debug " + this.getFromAddress());
			int temp = this.fromAddress.substring(0, len2).compareTo(
					fromMail.getFromAddress());
			return temp;
		} else {
			len2 = this.getFromAddress().length();
			return this.fromAddress.compareTo(fromMail.getFromAddress()
					.substring(0, len2));
		}
	}

}
