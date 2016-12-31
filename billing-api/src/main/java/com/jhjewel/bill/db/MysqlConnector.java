package com.jhjewel.bill.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;

public class MysqlConnector {
	private static AsyncSQLClient mySQLClient=null;
	private static JsonObject mySQLClientConfig = new JsonObject().put("host",
			"localhost").put("username", "root").put("password", "root").put("database","bill");

	public static AsyncSQLClient getAsyncSQLClient(Vertx vertx) {
		// TODO Auto-generated method stub
		if (mySQLClient == null) {
			mySQLClient = MySQLClient.createShared(vertx, mySQLClientConfig);
		}
		return mySQLClient;
	}
	
	

}
