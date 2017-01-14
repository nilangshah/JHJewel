package com.jhjewel.billing;

import io.vertx.core.Vertx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.jhjewel.billing.api.DealerApi;
import com.jhjewel.billing.api.UserApi;

public class AppStarter {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
		final Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new UserApi(context));
		vertx.deployVerticle(new AppRouter(context));
		vertx.deployVerticle(new DealerApi(context));
		System.out.println("Deployment done");
	}
}
