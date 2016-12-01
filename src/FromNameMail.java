
import java.io.Serializable;

/**
 * FromNameMail used by the FromNameSearchTree for searching the string amongst any word in the Names of From Addresses
 * @author HemanthKumarTirupati
 *
 */

public class FromNameMail implements MailTree, Serializable{
	private static final long serialVersionUID = -8465116787318420577L;
	private String subFromName;
	private long UID;
	private boolean valid;
	private String fromAddress;
	private String subject;
	private String date;
	


	/**
	 * Here toAddress must be parsed before sending
	 * All the other parameters can be sent raw
	 * @param subFromName
	 * @param valid
	 * @param UID
	 * @param fromAddress
	 * @param subject
	 * @param date
	 */
	public FromNameMail(String subFromName, String valid, String UID, String fromAddress, String subject, String date){
		this.subFromName = subFromName.toLowerCase();
		this.fromAddress = Include.parseAddress(fromAddress, 6);
		this.subject = subject;
		this.date = date;
		this.UID = Long.parseLong(UID);
		valid = valid.substring(7);
		if(valid.equals("1")){
			this.valid = true;
		}
		else{
			this.valid = false;
		}
	}

	/**
	 * Getters, Setters and Override methods
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

	public String getSubFromName() {
		return subFromName;
	}

	public void setSubFromName(String subFromName) {
		this.subFromName = subFromName;
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
		FromNameMail fromNameMail = (FromNameMail) o;
		return this.subFromName.compareTo(fromNameMail.subFromName);
	}

	@Override
	public int compareWith(MailTree mail) {
		// TODO Auto-generated method stub
		FromNameMail fromNameMail = (FromNameMail) mail;
		return this.subFromName.compareTo(fromNameMail.subFromName);
	}

	
}
