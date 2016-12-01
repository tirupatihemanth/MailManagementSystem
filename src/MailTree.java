
/**
 * Interface to be implemented by all the *Mail used by the nodes in Search trees
 * @author HemanthKumarTirupati
 *
 */

public interface MailTree extends Comparable<Object> {

	public Long getUID();

	public void setUID(Long UID);

	public boolean isValid();

	public void setValid(boolean valid);
	
	public int compareWith(MailTree mail);
	
	public String getFromAddress();
	
	public String getSubject();
	
	public String getDate();
	
}
