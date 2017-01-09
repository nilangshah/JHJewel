package com.jhjewel.bill;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhjewel.bill.api.DealerAPI;
import com.jhjewel.bill.db.UserDAO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxException;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.handler.impl.FormLoginHandlerImpl;
import io.vertx.ext.web.sstore.LocalSessionStore;

/**
 * 
 * @author nilshah
 *
 */
public class Server extends AbstractVerticle {

	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}

	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);

	@Override
	public void start() throws Exception {
		LOGGER.debug("Billing service Init");
		Router router = Router.router(vertx);

		// We need cookies, sessions and request bodies
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		
		AuthProvider authProvider = new CustomAuthProvider();
		// We need a user session handler too to make sure the user is stored in
		// the session between requests
		router.route().handler(UserSessionHandler.create(authProvider));

		// Any requests to URI starting '/private/' require login
		router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/login.html"));

		// Serve the static private pages from directory 'private'
		//router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));
		
		router.route("/api/authenticate*").handler(BodyHandler.create());
		// Handles the actual login
		
		//router.route(HttpMethod.POST, "/api/authenticate/").handler(this::login);
		router.route(HttpMethod.POST, "/api/authenticate/").handler(FormLoginHandler.create(authProvider));
		new DealerAPI(router);

		// Implement logout
		router.route("/logout").handler(context -> {
			context.clearUser();
			// Redirect back to the index page
			context.response().putHeader("location", "/").setStatusCode(302).end();
		});

		// Serve the non private static pages
		router.route().handler(StaticHandler.create());
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

//	private void login(RoutingContext rc) {
//		JsonObject authInfo = rc.getBodyAsJson();
//		String username = authInfo.getString("username");
//		if (username == null) {
//			LOGGER.warn("Username missing");
//			rc.response().setStatusCode(401).end();
//			return;
//		}
//		String password = authInfo.getString("password");
//		if (password == null) {
//			LOGGER.warn("Password missing");
//			rc.response().setStatusCode(401).end();
//			return;
//		}
//		try {
//			com.jhjewel.bill.model.User u = UserDAO.getInstance().getUserById(username);
//			if (u == null) {
//				// Unknown user/password
//				LOGGER.warn("Invalid Username/Password");
//				rc.response().setStatusCode(403).end();
//			} else {
//				String hashedStoredPwd = u.getPassword();
//				String salt = u.getPassword_salt();
//				String hashedPassword = computeHash(password, salt);
//				if (hashedStoredPwd.equals(hashedPassword)) {
//					rc.setUser(new CustomUser(u.getUsername(), null));
//					rc.response().setStatusCode(200).end();
//				} else {
//					LOGGER.warn("Password do not match");
//					rc.response().setStatusCode(403).end();
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.warn("Sql Error Occured:", e);
//			rc.response().setStatusCode(401).end();
//		}
//	}
//
//	public String computeHash(String password, String salt) {
//		return computeHash(password, salt, "SHA-512");
//	}
//
//	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
//
//	public static String bytesToHex(byte[] bytes) {
//		char[] chars = new char[bytes.length * 2];
//		for (int i = 0; i < bytes.length; i++) {
//			int x = 0xFF & bytes[i];
//			chars[i * 2] = HEX_CHARS[x >>> 4];
//			chars[1 + i * 2] = HEX_CHARS[0x0F & x];
//		}
//		return new String(chars);
//	}
//
//	public static String computeHash(String password, String salt, String algo) {
//		try {
//			MessageDigest md = MessageDigest.getInstance(algo);
//			String concat = (salt == null ? "" : salt) + password;
//			byte[] bHash = md.digest(concat.getBytes(StandardCharsets.UTF_8));
//			return bytesToHex(bHash);
//		} catch (NoSuchAlgorithmException e) {
//			throw new VertxException(e);
//		}
//	}

}