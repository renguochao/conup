package cn.edu.nju.moon.conup.sample.portal2.services;

import org.oasisopen.sca.annotation.Remotable;

@Remotable
public interface TokenService {
	String getToken(String cred);
}