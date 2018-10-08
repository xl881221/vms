package fmss.action;

public class RefresherAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String refresher() {
		if (log.isDebugEnabled())
			log.debug("refresh request from page");
		return SUCCESS;
	}
}
