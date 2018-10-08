package fmss.action;

public class HomeDataXmlAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4155338576766430456L;

	public String execute() throws Exception {
		if("getVersion".equals(request.getParameter("type"))){
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("");	
			response.getWriter().close();
		}
		return null;
	}
}
