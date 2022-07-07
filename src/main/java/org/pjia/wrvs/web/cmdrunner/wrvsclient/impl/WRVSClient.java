package org.pjia.wrvs.web.cmdrunner.wrvsclient.impl;

import java.io.IOException;
import java.util.Arrays;

import org.pjia.wrvs.web.cmdrunner.WRVSClientProperties;
import org.pjia.wrvs.web.cmdrunner.wrvsclient.IWRVSClient;
import org.springframework.lang.Nullable;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.util.APIVersion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WRVSClient implements IWRVSClient {

	private final Session session;
	private final IntegrationPoint point;
	private WRVSClientProperties properties;

	public WRVSClient(WRVSClientProperties properties) throws Exception {
		this.properties = properties;
		point = IntegrationPointFactory.getInstance().createIntegrationPoint(
				properties.getHostname(), 
				Integer.valueOf(properties.getPort()), APIVersion.API_4_16);
		session = point.createNamedSession("wrvs.client", 
				APIVersion.API_4_16, 
				properties.getConnectUser(), 
				properties.getPassword());
		session.setAutoReconnect(true);
		log.info("{} is ready", session);
	}

	@Override
	public Response execute(Command command, @Nullable String impersonationUser) throws APIException {
		CmdRunner cmdRunner = null;
		try {
			cmdRunner = session.createCmdRunner();
			if(impersonationUser != null) {
				cmdRunner.setDefaultImpersonationUser(impersonationUser);			
			}
			log.info(Arrays.toString(command.toStringArray()));
			Response response = cmdRunner.execute(command);
			return response;
		} finally {
			if(cmdRunner != null) {
				cmdRunner.release();
			}
		}
	}

	@Override
	public void close() {
		if(session != null) {
			try {
				session.release();
			} catch (IOException e) {
			} catch (APIException e) {
			}
			log.info("{} is released", session);
		}
	}

	@Override
	public Response connect(String user, String password) throws APIException, IOException {
		Session namedSession = point.createNamedSession("wrvs.client", 
				APIVersion.API_4_16, 
				user, 
				password);
		CmdRunner runner = namedSession.createCmdRunner();
		Command command = new Command("im", "connect");
		Response response = runner.execute(command);
		runner.release();
		namedSession.release();
		return response;
	}
	
	@Override
	public WRVSClientProperties getProps() {
		return properties;
	}

}
