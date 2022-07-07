package org.pjia.wrvs.web.cmdrunner.wrvsclient;

import java.io.IOException;

import org.pjia.wrvs.web.cmdrunner.WRVSClientProperties;

import com.mks.api.Command;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

/**
 * 
 * 调用WRVS远程服务的客户端
 * 
 * @author pjia
 *
 */
public interface IWRVSClient {
	/**
	 * 
	 * 执行命令
	 * 
	 * @param command Command
	 * @param impersonationUser impersonationUser
	 * @return Response
	 * @throws APIException APIException
	 */
	Response execute(Command command, String impersonationUser) throws APIException;
	/**
	 * 
	 * 关闭客户端
	 * 
	 */
	void close();
	
	/**
	 * 登录用，检查用户密码是否正确
	 * 
	 * @param user user
	 * @param password password
	 * @return Response
	 * @throws APIException
	 * @throws IOException 
	 */
	Response connect(String user, String password) throws APIException, IOException;
	
	/**
	 * 获取客户端参数
	 * 
	 * @return
	 */
	public WRVSClientProperties getProps();
}
