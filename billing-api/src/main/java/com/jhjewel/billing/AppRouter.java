package com.jhjewel.billing;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jhjewel.billing.api.DealerApi;
import com.jhjewel.billing.api.UserApi;
import com.jhjewel.billing.auth.CustomAuthProvider;
import com.jhjewel.billing.db.UserService;

/**
 * 
 * @author nilshah
 *
 */
public class AppRouter extends AbstractVerticle {

	private static Logger LOGGER = LoggerFactory.getLogger(AppRouter.class);
	private final UserService service;

	public AppRouter(final ApplicationContext context) {
		service = (UserService) context.getBean("userService");
	}

	@Override
	public void start() throws Exception {
		LOGGER.debug("Billing service Init");
		Router router = Router.router(vertx);

		// We need cookies, sessions and request bodies
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

		AuthProvider authProvider = new CustomAuthProvider(service);
		// We need a user session handler too to make sure the user is stored in
		// the session between requests
		router.route().handler(UserSessionHandler.create(authProvider));

		// Any requests to URI starting '/private/' require login
		router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/login.html"));
		router.route("/").handler(RedirectAuthHandler.create(authProvider, "/login.html"));

		// Serve the static private pages from directory 'private'
		// router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));

		router.route("/api/authenticate*").handler(BodyHandler.create());
		// Handles the actual login

		// router.route(HttpMethod.POST,
		// "/api/authenticate/").handler(this::login);
		router.route(HttpMethod.POST, "/api/authenticate/").handler(FormLoginHandler.create(authProvider));

		// Implement logout
		router.route("/logout").handler(context -> {
			context.clearUser();
			// Redirect back to the index page
				context.response().putHeader("location", "/login.html").setStatusCode(302).end();
			});

		addUserApis(router);
		addDealerApis(router);
		// Serve the non private static pages
		router.route().handler(StaticHandler.create());
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}

	private void addUserApis(Router router) {
		router.route(HttpMethod.POST, "/users").handler(new Handler<RoutingContext>() {
			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().<String> send(UserApi.ADD_USER, rc.getBodyAsJson(), result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.CREATED.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}
		});

		router.route(HttpMethod.GET, "/users").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().send(UserApi.ALL_USER, "", result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end((String) result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}

		});
		router.route(HttpMethod.DELETE, "/users/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().<String> send(UserApi.DELETE_USER, rc.request().getParam("id"), result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}
		});
		router.route(HttpMethod.GET, "/users/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				JsonObject json = new JsonObject();
				json.put("userId", rc.request().getParam("id"));
				vertx.eventBus().<String> send(UserApi.GET_USER, json, result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});
			}
		});
		router.route(HttpMethod.PUT, "/users/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				JsonObject bodyAsJson = rc.getBodyAsJson();
				bodyAsJson.put("id", rc.request().getParam("id"));
				vertx.eventBus().<String> send(UserApi.UPDATE_USER, bodyAsJson, result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});
			}
		});

	}

	private void addDealerApis(Router router) {
		router.route(HttpMethod.POST, "/dealers").handler(new Handler<RoutingContext>() {
			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().<String> send(DealerApi.ADD_DEALER, rc.getBodyAsJson(), result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.CREATED.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}
		});

		router.route(HttpMethod.GET, "/dealers").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().send(DealerApi.ALL_DEALER, "", result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end((String) result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}

		});
		router.route(HttpMethod.DELETE, "/dealers/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				vertx.eventBus().<String> send(DealerApi.DELETE_DEALER, rc.request().getParam("id"), result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});

			}
		});
		router.route(HttpMethod.GET, "/dealers/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				JsonObject json = new JsonObject();
				json.put("dealerId", rc.request().getParam("id"));
				vertx.eventBus().<String> send(DealerApi.GET_DEALER, json, result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});
			}
		});
		router.route(HttpMethod.PUT, "/dealers/:id").handler(new Handler<RoutingContext>() {

			@Override
			public void handle(RoutingContext rc) {
				setResponseHeaders(rc);
				JsonObject request = rc.getBodyAsJson();
				request.put("id", rc.request().getParam("id"));
				vertx.eventBus().<String> send(DealerApi.UPDATE_DEALER, request, result -> {
					if (result.succeeded()) {
						rc.response().setStatusCode(HttpResponseStatus.OK.code()).end(result.result().body());
					} else {
						rc.response().setStatusCode(((ReplyException) result.cause()).failureCode()).end(result.cause().getMessage());
					}
				});
			}
		});

	}

	private void setResponseHeaders(RoutingContext rc) {
		rc.response().putHeader("content-type", "application/json; charset=utf-8");
	}

}