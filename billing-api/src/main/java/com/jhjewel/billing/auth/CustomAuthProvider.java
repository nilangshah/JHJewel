package com.jhjewel.billing.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhjewel.billing.db.UserService;
import com.jhjewel.util.AuthUtil;

public class CustomAuthProvider implements AuthProvider {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomAuthProvider.class);
	private final UserService service;

	public CustomAuthProvider(final UserService service) {
		this.service = service;
	}

	@Override
	public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
		String username = authInfo.getString("username");
		if (username == null) {
			LOGGER.warn("Username missing");
			resultHandler.handle(Future.failedFuture("authInfo must contain username in 'username' field"));
			return;
		}
		String password = authInfo.getString("password");
		if (password == null) {
			LOGGER.warn("Password missing");
			resultHandler.handle(Future.failedFuture("authInfo must contain password in 'password' field"));
			return;
		}
		try {
			com.jhjewel.billing.model.User u = service.getUserByName(username);
			if (u == null) {
				// Unknown user/password
				LOGGER.warn("Invalid Username/Password");
				resultHandler.handle(Future.failedFuture("Invalid username/password"));
			} else {
				String hashedStoredPwd = u.getPassword();
				String salt = u.getPasswordSalt();
				String hashedPassword = AuthUtil.computeHash(password, salt);
				LOGGER.warn(password + ":"+ hashedPassword + ":"+hashedStoredPwd);
				if (hashedStoredPwd.equals(hashedPassword)) {
					resultHandler.handle(Future.succeededFuture(new CustomUser(username, this)));
				} else {
					LOGGER.warn("Password do not match");
					resultHandler.handle(Future.failedFuture("Invalid username/password"));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Sql Error Occured:", e);
			// More than one row returned!
			resultHandler.handle(Future.failedFuture("Failure in authentication"));
		}
	}

}
