package com.jhjewel.bill;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

public class CustomUser extends AbstractUser {

	private String username;
	private JsonObject principal;

	public CustomUser() {

	}

	public CustomUser(String username, AuthProvider authProvider) {
		this.username = username;
	}

	@Override
	public JsonObject principal() {
		if (principal == null) {
			principal = new JsonObject().put("username", username);
		}
		return principal;
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		if (authProvider instanceof CustomAuthProvider) {
		} else {
			throw new IllegalArgumentException("Not a CustomAuthProvider");
		}

	}

	@Override
	protected void doIsPermitted(String permission,
			Handler<AsyncResult<Boolean>> resultHandler) {
		resultHandler.handle(Future.succeededFuture(true));
	}

}
