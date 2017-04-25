package service;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.slf4j.LoggerFactory;

public class ServerPasswordCallback implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		System.out.println("CALLING " + getClass());
		LoggerFactory.getLogger(getClass()).info("CALLING " + getClass());
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		if ("joe".equals(pc.getIdentifier())) {
			pc.setPassword("joespassword");
		}
	}
}
