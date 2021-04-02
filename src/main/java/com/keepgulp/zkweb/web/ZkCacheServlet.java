package com.keepgulp.zkweb.web;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keepgulp.zkweb.util.ZkCache;
import com.keepgulp.zkweb.util.ZkCfgFactory;
import com.keepgulp.zkweb.util.ZkManager;
import com.keepgulp.zkweb.util.ZkManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "cacheServlet",urlPatterns = "/cache/*")
public class ZkCacheServlet extends HttpServlet {
	
	private static final Logger log = LoggerFactory.getLogger(ZkCacheServlet.class);
	
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ZkCacheServlet() {
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ZkManager zkManager;
		for(Map<String , Object> m : ZkCfgFactory.createZkCfgManager().query()){
			zkManager= ZkCache.get(m.get("ID").toString());
			if(zkManager==null) {
				log.info("zk info: id={},connectstr={},timeout={}",m.get("ID"),m.get("CONNECTSTR"),m.get("SESSIONTIMEOUT"));
				ZkCache.put(m.get("ID").toString(), ZkManagerImpl.createZk().connect(m.get("CONNECTSTR").toString(), Integer.parseInt(m.get("SESSIONTIMEOUT").toString())));
			}else {
				log.info("zk(exists) info: id={},connectstr={},timeout={}",m.get("ID"),m.get("CONNECTSTR"),m.get("SESSIONTIMEOUT"));
				zkManager.reconnect();
			}
		}
		
		//for(String key : ZkCache.get_cache().keySet()){
		//	log.info("key : {} , zk : {}",key,ZkCache.get(key));
		//}
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
	}

}
