package rvs.web.cmdrunner.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mks.api.response.APIException;

import rvs.web.cmdrunner.client.IWRVSClient;

@RestController
@RequestMapping("/session")
public class SessionController implements ISessionController {
	
	@Resource
	private IWRVSClient client;

	@Override
	@PostMapping("/create")
	public LoginVO login(HttpServletRequest request) {
		LoginVO loginVO = new LoginVO();
		String authorization = request.getHeader("authorization");
		byte[] decode = Base64.getDecoder().decode(authorization.replace("Basic ", ""));
		String string = new String(decode);
		String[] split = string.split(":");
		String user = split[0];
		String pwd = split[1];
		loginVO.setUser(user);
		try {
			client.connect(user, pwd);
			Algorithm algorithm = Algorithm.HMAC256(client.getProps().getSecretKey());
			Calendar calendar = Calendar.getInstance();
			Date time0 = calendar.getTime();
			calendar.add(Calendar.MINUTE, 15);
			Date time1 = calendar.getTime();
			String token = JWT.create()
				.withIssuer("wrvs.web0")
				.withIssuedAt(time0)
				.withExpiresAt(time1)
				.withClaim("wrvs.user0", user)
				.sign(algorithm);
			loginVO.setToken(token);
		} catch (APIException e) {
			loginVO.setMessage(e.getMessage());
		} catch (IOException e) {
			loginVO.setMessage(e.getMessage());
		}
		return loginVO;
	}
}
