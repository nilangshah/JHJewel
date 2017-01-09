package com.jhjewel.bill.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jhjewel.bill.db.DealerDAO;
import com.jhjewel.bill.model.Dealer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class DealerAPI {
	private static Logger LOGGER = LoggerFactory.getLogger(DealerAPI.class);

	public DealerAPI(Router router) {
		addDealerRoutes(router);
	}

	private void addDealerRoutes(Router router) {
		LOGGER.info("Adding dealer routes");
		router.route(HttpMethod.POST, "/dealers").handler(this::addDealer);
		router.route(HttpMethod.GET, "/dealers").handler(this::getAllDealers);
		router.route(HttpMethod.DELETE, "/dealers/:id").handler(this::deleateDealer);
		router.route(HttpMethod.GET, "/dealers/:id").handler(this::getDealer);
		router.route(HttpMethod.PUT, "/dealers/:id").handler(this::updateDealer);
	}

	private void deleateDealer(RoutingContext rc) {
		String id = rc.request().getParam("id");
		if (id == null) {
			LOGGER.info("Nothing to delerte. DealerId is null");
			rc.response().setStatusCode(400).end();
		} else {
			Integer dealerId = Integer.valueOf(id);
			LOGGER.info("Deleting dealer id={}", dealerId);
			DealerDAO.getInstance().deleteDealer(dealerId);
		}
		rc.response().setStatusCode(204).end();
	}

	public void getAllDealers(RoutingContext rc) {
		LOGGER.info("Fetching all dealers");
		rc.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(DealerDAO.getInstance().getAllDealers()));
	}

	private void updateDealer(RoutingContext rc) {
		final String id = rc.request().getParam("id");
		JsonObject json = rc.getBodyAsJson();
		if (id == null || json == null) {
			LOGGER.info("Nothing to update. DealerId is null");
			rc.response().setStatusCode(400).end();
		} else {
			final Integer dealerId = Integer.valueOf(id);
			Dealer d = DealerDAO.getInstance().getDealerById(dealerId);
			if (d == null) {
				LOGGER.info("Nothing to update. No dealer found id={}", dealerId);
				rc.response().setStatusCode(404).end();
			} else {
				LOGGER.info("Updating dealer id={}", dealerId);
				d.setAddress(json.getString("address"));
				d.setCompanyName(json.getString("companyName"));
				d.setDealerName(json.getString("dealerName"));
				d.setDealerType(json.getString("dealerType"));
				d.setMobileNo(json.getString("mobileNo"));
				d.setPanNo(json.getString("panNo"));
				d.setVatTin(json.getString("vatTin"));
				DealerDAO.getInstance().updateDealer(d);
				rc.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(d));
			}
		}
	}

	public void getDealer(RoutingContext rc) {
		final String id = rc.request().getParam("id");
		if (id == null) {
			LOGGER.info("Nothing to get. DealerId is null");
			rc.response().setStatusCode(400).end();
		} else {
			final Integer dealerId = Integer.valueOf(id);
			LOGGER.info("Fetch dealer id={}", dealerId);
			Dealer d = DealerDAO.getInstance().getDealerById(dealerId);
			if (d == null) {
				rc.response().setStatusCode(404).end();
			} else {
				rc.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(d));
			}
		}
	}

	public void addDealer(RoutingContext rc) {
		JsonObject json = rc.getBodyAsJson();
		try {
			String dealerName = json.getString("dealerName");
			if (dealerName == null) {
				LOGGER.warn("DealerName is missing");
				rc.response().setStatusCode(500).end("DealerName is missing");
				return;
			}
			String companyName = json.getString("companyName");
			if (companyName == null) {
				LOGGER.warn("companyName is missing");
				rc.response().setStatusCode(500).end("CompanyName is missing");
				return;
			}
			Dealer d = new Dealer();
			d.setAddress(json.getString("address"));
			d.setCompanyName(json.getString("companyName"));
			d.setDealerName(json.getString("dealerName"));
			d.setDealerType(json.getString("dealerType"));
			d.setMobileNo(json.getString("mobileNo"));
			d.setPanNo(json.getString("panNo"));
			d.setVatTin(json.getString("vatTin"));
			DealerDAO.getInstance().addDealer(d);
			rc.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(d));
		} catch (Exception e) {
			LOGGER.error("Error occured while adding Dealer", e);
			rc.response().setStatusCode(500).end("Failed to add dealer");
		}
	}
}
