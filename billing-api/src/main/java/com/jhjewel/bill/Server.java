package com.jhjewel.bill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jhjewel.bill.db.MysqlConnector;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}

	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);

	@Override
	public void start() throws Exception {
		LOGGER.debug("Bill service Init");
		Router router = Router.router(vertx);
		AsyncSQLClient sqlClient = MysqlConnector.getAsyncSQLClient(vertx);
		if (sqlClient == null) {
			LOGGER.warn("Mysql is not initialized");
		}
		// We need cookies, sessions and request bodies
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(
				SessionHandler.create(LocalSessionStore.create(vertx)));

		AuthProvider authProvider = new CustomAuthProvider(sqlClient);
		// We need a user session handler too to make sure the user is stored in
		// the session between requests
		
		router.route().handler(UserSessionHandler.create(authProvider));
		
		// Any requests to URI starting '/private/' require login
		router.route("/private/*").handler(
				RedirectAuthHandler.create(authProvider, "/loginpage.html"));

		// Serve the static private pages from directory 'private'
		router.route("/private/*").handler(
				StaticHandler.create().setCachingEnabled(false)
						.setWebRoot("private"));

		router.route("/api/authenticate*").handler(BodyHandler.create());
		// Handles the actual login
		router.route(HttpMethod.POST, "/api/authenticate/").handler(
				FormLoginHandler.create(authProvider));

		// Implement logout
		router.route("/logout").handler(context -> {
			context.clearUser();
			// Redirect back to the index page
				context.response().putHeader("location", "/")
						.setStatusCode(302).end();
			});

		// Serve the non private static pages
		router.route().handler(StaticHandler.create());

		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}