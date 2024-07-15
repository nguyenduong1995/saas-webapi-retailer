/**
 * LoginForm.java
 * @author     duyvk
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LoginForm. <<< Screen 001. (S001) Login form information.
 * 
 * @author duyvk
 */
public class LoginForm implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Map<String, String> fieldMap = new LinkedHashMap<>();

    static {
        fieldMap.put("loginName", "S01.text.loginName");
        fieldMap.put("password", "S01.text.password");
    }

    private String loginName;

    private String password;

    /**
     * get value of <b>loginName</b>.
     * 
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Set value to <b>loginName</b>.
     * 
     * @param loginName
     *            the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * get value of <b>password</b>.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set value to <b>password</b>.
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginForm [loginName=" + loginName + ", password=" + password + "]";
    }
    
}
