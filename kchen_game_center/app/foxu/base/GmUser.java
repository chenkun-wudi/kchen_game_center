package foxu.base;

/**
 * GM账号
 * 
 * @author kunchen
 *
 */
public class GmUser {
	/* fields */
	/** 账号 */
	String account;
	/** 密码 */
	String pwd;
	/** 权限 */
	int power;

	/* properties */
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
}
