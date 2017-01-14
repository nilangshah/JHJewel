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
import com.jhjewel.billing.db.DealerService;
import com.jhjewel.billing.exception.DataNotFoundException;
import com.jhjewel.billing.model.Dealer;

public class DealerApi extends AbstractVerticle {

	private static Logger LOGGER = LoggerFactory.getLogger(DealerApi.class);
	public static final String ALL_DEALER = "billing.all.dealer";
	public static final String GET_DEALER = "billing.get.dealer";
	public static final String ADD_DEALER = "billing.add.dealer";
	public static final String DELETE_DEALER = "billing.delete.dealer";
	public static final String UPDATE_DEALER = "billing.update.dealer";

	// Reuse the Vert.x Mapper, alternatively you can use your own.
	private final ObjectMapper mapper = Json.mapper;

	private final DealerService service;

	public DealerApi(final ApplicationContext context) {
		service = (DealerService) context.getBean("dealerService");
	}

	private Handler<Message<JsonObject>> allDealersHandler(DealerService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			try {
				List<Dealer> dealers = service.getAllDealers();
				LOGGER.info("Fetching all dealers:" + dealers);
				future.complete(Json.encodePrettily((dealers)));
			} catch (Exception e) {
				LOGGER.error("Failed to fetch all dealers: ", e);
				future.fail("Failed to fetch all dealers");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> addDealerHandler(DealerService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Add new dealer");
			try {
				JsonObject json = msg.body();
				Dealer d = mapper.readValue(json.toString(), Dealer.class);
				future.complete(Json.encodePrettily(service.addDealer(d)));
			} catch (DataIntegrityViolationException e) {
				LOGGER.error("Failed to add dealer: ", e);
				future.fail("Dealer already exists");
			} catch (Exception e) {
				LOGGER.error("Failed to add dealer: ", e);
				future.fail("Failed to add dealer");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> updateDealerHandler(DealerService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Update dealer");
			try {
				JsonObject json = msg.body();
				Dealer dealer = mapper.readValue(json.toString(), Dealer.class);
				future.complete(mapper.writeValueAsString(service.updateDealer(dealer)));
			} catch (DataNotFoundException e) {
				LOGGER.error("Failed to update dealer: ", e);
				future.fail("Dealer not found");
			} catch (Exception e) {
				LOGGER.error("Failed to update dealer: ", e);
				future.fail("Failed to update dealer");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<String>> deleteDealerHandler(DealerService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			LOGGER.info("Delete dealers");
			try {
				int dealerId = Integer.parseInt(msg.body());
				service.deleteDealer(dealerId);
				future.complete(Json.encodePrettily("Dealer deleted successfully"));
			} catch (EmptyResultDataAccessException e) {
				LOGGER.error("Failed to delete dealer: ", e);
				future.fail("Dealer does not exist");
			} catch (Exception e) {
				LOGGER.error("Failed to delete dealer: ", e);
				future.fail("Failed to delete dealer");
			}
		}, result -> {
			if (result.succeeded()) {
				msg.reply(result.result());
			} else {
				msg.fail(HttpResponseStatus.NOT_FOUND.code(), Json.encodePrettily(result.cause().getMessage()));
			}
		});
	}

	private Handler<Message<JsonObject>> getDealerHandler(DealerService service) {
		return msg -> vertx.<String> executeBlocking(future -> {
			try {
				String dealerIdStr = msg.body().getString("dealerId");
				int dealerId = Integer.parseInt(dealerIdStr);
				LOGGER.info(String.format("Fetch dealer: %s", dealerIdStr));
				Dealer dealer = service.getUser(dealerId);
				if (dealer == null) {
					future.fail("Dealer not found");
				} else {
					future.complete(Json.encodePrettily(dealer));
				}
			} catch (Exception e) {
				LOGGER.error("Failed to get dealer by id: ", e);
				future.fail("Dealer not found");
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
		LOGGER.info("DealerAPI vertical is deployed");
		EventBus eb = vertx.eventBus();
		eb.<JsonObject> consumer(ALL_DEALER).handler(allDealersHandler(service));
		eb.<JsonObject> consumer(ADD_DEALER).handler(addDealerHandler(service));
		eb.<JsonObject> consumer(UPDATE_DEALER).handler(updateDealerHandler(service));
		eb.<String> consumer(DELETE_DEALER).handler(deleteDealerHandler(service));
		eb.<JsonObject> consumer(GET_DEALER).handler(getDealerHandler(service));

	}

}
