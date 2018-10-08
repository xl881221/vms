package fmss.action;

public final class LoginManager {

    private static ThreadLocal loginCache = new ThreadLocal();

    public static void setLogin(Login login) {
    	loginCache.set(login);
    }

    public static Login getLogin() {
		if (null == loginCache.get()) {
		    loginCache.set(new Login());
		}
		return (Login) loginCache.get();
    }
	
	private LoginManager() {
	
    }

}
