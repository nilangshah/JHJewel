package com.jhjewel.billing.api;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhjewel.billing.db.UserService;
import com.jhjewel.billing.exception.DataNotFoundException;
import com.jhjewel.billing.model.User;

public class UserApi extends AbstractVerticle {

	private static Logger LOGGER = LoggerFactory.getLogger(UserApi.class);
	public static final String ALL_USER = "billing.all.user";
	public static final String GET_USER = "billing.get.user";
	public static final String ADD_USER = "billing.add.user";
	public static final String DELETE_USER = "billing.delete.user";
	public static final String UPDATE_USER = "billing.update.user";

	// Reuse the Vert.x Mapper, alternatively you can use your own.
	private final ObjectMapper mapper = Json.mapper;

	private final UserService service;

	public UserApi(final ApplicationContext context) {
		service = (UserService) context.getBean("userService");
	}

	private Handler<Message<JsonObject>> allUsersHandler(UserService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			try {
				List<User> users = service.getAllUsers();
				LOGGER.info("Fetching all users:" + users);
				future.complete(Json.encodePrettily((users)));
			} catch (Exception e) {
				LOGGER.error("Failed to fetch all users: ", e);
				future.fail("Failed to fetch all users");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> addUserHandler(UserService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Add new users");
			try {
				JsonObject json = msg.body();
				User u = mapper.readValue(json.toString(), User.class);
				future.complete(Json.encodePrettily(service.addUser(u)));
			} catch (DataIntegrityViolationException e) {
				LOGGER.error("Failed to add user: ", e);
				future.fail("User already exists");
			} catch (Exception e) {
				LOGGER.error("Failed to add user: ", e);
				future.fail("Failed to add user");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> updateUserHandler(UserService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Update user");
			try {
				JsonObject json = msg.body();
				User user = mapper.readValue(json.toString(), User.class);
				future.complete(mapper.writeValueAsString(service.updateUser(user)));
			} catch (DataNotFoundException e) {
				LOGGER.error("Failed to update user: ", e);
				future.fail("User not found");
			} catch (Exception e) {
				LOGGER.error("Failed to update user: ", e);
				future.fail("Failed to update user");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<String>> deleteUserHandler(UserService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Delete users");
			try {
				int userId = Integer.parseInt(msg.body());
				service.deleteUser(userId);
				future.complete(Json.encodePrettily("User deleted successfully"));
			} catch (EmptyResultDataAccessException e) {
				LOGGER.error("Failed to delete user: ", e);
				future.fail("User does not exist");
			} catch (Exception e) {
				LOGGER.error("Failed to delete user: ", e);
				future.fail("Failed to delete user");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> getUserHandler(UserService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			try {
				String userIdStr = msg.body().getString("userId");
				int userId = Integer.parseInt(userIdStr);
				LOGGER.info(String.format("Fetch user: %s", userIdStr));
				User user = service.getUser(userId);
				if (user == null) {
					future.fail("User not found");
				} else {
					future.complete(Json.encodePrettily(user));
				}
			} catch (Exception e) {
				LOGGER.error("Failed to get user by id: ", e);
				future.fail("User not found");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	@Override
	public void start() throws Exception {
		super.start();
		LOGGER.info("UserAPI vertical is deployed");
		EventBus eb = vertx.eventBus();
		eb.<JsonObject> consumer(ALL_USER).handler(allUsersHandler(service));
		eb.<JsonObject> consumer(ADD_USER).handler(addUserHandler(service));
		eb.<JsonObject> consumer(UPDATE_USER).handler(updateUserHandler(service));
		eb.<String> consumer(DELETE_USER).handler(deleteUserHandler(service));
		eb.<JsonObject> consumer(GET_USER).handler(getUserHandler(service));

	}

}
