package com.jhjewel.bill;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhjewel.bill.db.UserDAO;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

public class CustomAuthProvider implements AuthProvider {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomAuthProvider.class);
	String DEFAULT_AUTHENTICATE_QUERY = "SELECT PASSWORD, PASSWORD_SALT FROM USER WHERE USERNAME = ?";

	public CustomAuthProvider() {
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
			//System.out.println(new UserDAO().getAllUsers());
			com.jhjewel.bill.model.User u = new UserDAO().getUserById(username);
			//resultHandler.handle(Future.succeededFuture(new CustomUser(username, this)));
			if (u == null) {
				// Unknown user/password
				LOGGER.warn("Invalid Username/Password");
				resultHandler.handle(Future.failedFuture("Invalid username/password"));
			} else {
				String hashedStoredPwd = u.getPassword();
				String salt = u.getPassword_salt();
				String hashedPassword = computeHash(password, salt);
				if (hashedStoredPwd.equals(hashedPassword)) {
					resultHandler.handle(Future.succeededFuture(new CustomUser(username, this)));
				} else {
					LOGGER.warn("Password do not match");
					resultHandler.handle(Future.failedFuture("Invalid username/password"));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Sql Error Occured:",e);
			// More than one row returned!
			resultHandler.handle(Future.failedFuture("Failure in authentication"));
		}
	}

	public String computeHash(String password, String salt) {
		return computeHash(password, salt, "SHA-512");
	}

	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] chars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int x = 0xFF & bytes[i];
			chars[i * 2] = HEX_CHARS[x >>> 4];
			chars[1 + i * 2] = HEX_CHARS[0x0F & x];
		}
		return new String(chars);
	}

	public static String computeHash(String password, String salt, String algo) {
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			String concat = (salt == null ? "" : salt) + password;
			byte[] bHash = md.digest(concat.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(bHash);
		} catch (NoSuchAlgorithmException e) {
			throw new VertxException(e);
		}
	}
}
