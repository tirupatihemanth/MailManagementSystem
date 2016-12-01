import java.io.Serializable;

/**
 * Used by the subject Mail Tree for each word of the subject in a given mail A
 * single mail can have many words in a subject and can have many subject mail
 * classes
 * 
 * @author HemanthKumarTirupati
 *
 */

public class SubjectMail implements MailTree, Serializable {

	private static final long serialVersionUID = -8465116787318420577L;
	private String subSubject;
	private long UID;
	private boolean valid;
	private String fromAddress;
	private String subject;
	private String date;

	/**
	 * subject is any word of the subject of a mail
	 * 
	 * @param subSubject
	 * @param valid
	 * @param UID
	 * @param fromAddress
	 * @param subject
	 * @param date
	 */
	public SubjectMail(String subSubject, String valid, String UID,
			String fromAddress, String subject, String date) {
		this.subSubject = subSubject.toLowerCase();
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

	public String getSubSubject() {
		return subSubject;
	}

	public void setSubSubject(String subSubject) {
		this.subSubject = subSubject;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		SubjectMail subjectMail = (SubjectMail) o;
		return this.subSubject.compareTo(subjectMail.subSubject);
	}

	@Override
	public int compareWith(MailTree mail) {
		// TODO Auto-generated method stub
		SubjectMail subjectMail = (SubjectMail) mail;
		return this.subSubject.compareTo(subjectMail.subSubject);
	}

	@Override
	public String getFromAddress() {
		// TODO Auto-generated method stub
		return this.fromAddress;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return this.subject;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return this.date;
	}

}
