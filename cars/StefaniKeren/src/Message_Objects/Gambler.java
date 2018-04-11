package Message_Objects;
import java.io.Serializable;

public class Gambler implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String password;
	private String cardNumber;
	private String permissionType;
	private int windowType; // 0 = Log In;  1 = Sign In
	
	
	public Gambler(String id, String name, String password, String cardNumber, String permissionType, int windowType) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.cardNumber = cardNumber;
		this.permissionType = permissionType;
		this.windowType = windowType;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getPermissionType() {
		return permissionType;
	}
	
	public int getWindowType() {
		return windowType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public void setWindowType(int windowType) {
		this.windowType = windowType;
	}

	@Override
	public String toString() {
		return "Gambler [id=" + id + ", name=" + name + ", password=" + password + ", cardNumber=" + cardNumber
				+ ", permissionType=" + permissionType + "]";
	}
	
	
	
	
	

	
}
