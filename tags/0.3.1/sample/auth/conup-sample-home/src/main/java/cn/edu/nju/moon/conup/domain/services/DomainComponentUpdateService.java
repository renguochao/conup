package cn.edu.nju.moon.conup.domain.services;

import org.oasisopen.sca.annotation.Remotable;

@Remotable
public interface DomainComponentUpdateService {
	public boolean onDemandRequest(String targetComponent, String freenessSetup);
//	public String status(String targetComponent);
	boolean update();
}
