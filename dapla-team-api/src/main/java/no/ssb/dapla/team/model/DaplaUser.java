package no.ssb.dapla.team.model;

public class DaplaUser {
	
	private String name;
	private String email;
	private String email_short;
	
	public DaplaUser() {
		
	}
	
	public DaplaUser(String name, String email, String email_short) {
		super();
		this.name = name;
		this.email = email;
		this.email_short = email_short;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail_short() {
		return email_short;
	}
	public void setEmail_short(String email_short) {
		this.email_short = email_short;
	}

	
}
