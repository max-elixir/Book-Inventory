package gan022CS4743Assignment4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle{
	private static final Logger logger = LogManager.getLogger();
	private SQLClient dbClient;
	private String bearerToken;
	private static final String SQL_VERIFY_PASSWORD = "select Id, username from Users where username=? and password = ?";
	private static final String SQL_CREATE_SESSION = "Insert into Session (user_id, token, expiration) values (?, SHA2( CONCAT( NOW(), 'hidden password' ) , 256), DATE_ADD( NOW(), INTERVAL 1 MINUTE))";
	private static final String SQL_GET_SESSION = "Select token from Session where user_id = ? and expiration = DATE_ADD( NOW(), INTERVAL 1 MINUTE)";
	private static final String SQL_VERIFY_SESSION = "Select * from Session where token = ?";
	private static final String SQL_VERIFY_PERMISSION = "SELECT Users.Id, username, Permissions.permission, Permissions.allowed FROM Users join Permissions on user_id=Users.Id WHERE Users.Id = ?";
	private static final String SQL_GET_BOOKS = "Select title, publisher_name, year_published from Books join Publisher on publisher_id=Publisher.id order by publisher_name, title";
	private static final String DEST_FILE_PATH = "./book_report.xls";
	
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		logger.info("Starting verticle...");

		Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
		steps.setHandler(ar -> { 
			if(ar.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(ar.cause());
			}
		});
	}
	
	private Future<Void> prepareDatabase() {
		Promise<Void> promise = Promise.promise();
		logger.info("Preparing database...");
		JsonObject mySQLClientConfig = new JsonObject()
				.put("host", "easel2.fulgentcorp.com")
				.put("username", "gan022")
				.put("password", "3dOMbhKcLRQSoXl0cAdA")
				.put("database", "gan022");
		dbClient = MySQLClient.createShared(vertx, mySQLClientConfig);
		
		promise.complete();
		return promise.future();
	}
	
	private Future<Void> startHttpServer() {
		logger.info("Starting http server...");

		Promise<Void> promise = Promise.promise();
		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		
		router.get("/").handler(this::indexHandler);
		router.get("/login").handler(this::loginHandler);
		router.post("/reports/bookdetail").handler(this::bookHandler);
		
		server.requestHandler(router)
			.listen(8888, ar -> {
				if (ar.succeeded()) {
					logger.info("HTTP server running on port 8888");
					promise.complete();
				} else {
					logger.error("Could not start a HTTP server", ar.cause());
					promise.fail(ar.cause());
				}
			});

		return promise.future();
	}
	
	private void indexHandler(RoutingContext context) {
		context.response().putHeader("Content-Type", "text/html");
		StringBuilder output = new StringBuilder();
		output.append("<h1>Welcome to the Book Inventory login portal</h1>");
		output.append("Encryption is tough, please enter your encypted password here");
		output.append("<div><form action=\"/login\" method=\"get\">");
		output.append("		<input type=\"text\" name=\"username\" placeholder=\"login\">\r\n" + 
				"      		<input type=\"password\" name=\"password\" placeholder=\"password\">\r\n" +  
				"      		<button type=\"submit\" >Login</button>\r\n" + 
				"      </div>\r\n" + 
				"    </form>\r\n"+ 
				"<div><form action=\"/reports/bookdetail\" method=\"post\">"
				+ "<br>Get report\r\n"
				+ "<input name=\"Authorization\" type=\"hidden\" value=\""+bearerToken+"\">"
				+ "<button type=\"submit\" >Report</button>"
				+ "</div></form>"  + 
				"  </div>");
		context.response().end(output.toString());
	}
	
	// sha2('1234', 255)
	
	
	private void loginHandler(RoutingContext context) {
		context.response().putHeader("Content-Type", "application/json");
		String username = context.request().getParam("username");
		String password = context.request().getParam("password");
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
			} else {
				logger.info("Verifing user "+username+" from database...");
				SQLConnection connection = ar.result();
				JsonArray params = new JsonArray().add(username);
				params.add(password);
				connection.queryWithParams(SQL_VERIFY_PASSWORD,
					params, result -> {
					/*if(result.failed()) {	} else {*/
					List<JsonArray> rows = result.result().getResults();
					if(rows.size() < 1) {
						connection.close();
						logger.error("Invalid login credentials");
						context.response().putHeader("Content-Type", "text/html");
						context.response().setStatusCode(401);
						context.fail(401);
						context.response().end();
					} else {
						connection.updateWithParams(SQL_CREATE_SESSION, new JsonArray().add(rows.get(0).getInteger(0)), result2 -> {
							if (result2.succeeded()) {
								connection.queryWithParams(SQL_GET_SESSION, new JsonArray().add(rows.get(0).getInteger(0)), result3 -> {
									logger.info("creating session...");
									bearerToken = result3.result().getResults().get(0).getString(0);
									
									JsonObject carJson = new JsonObject();
									carJson.put("response", "ok"); 
									carJson.put("session token", bearerToken);
									
									logger.info("Session token: " + bearerToken);
									
									context.response().setStatusCode(303);
									//context.response().putHeader("Location", "reports/bookdetail");
									
									context.response().end(carJson.encode());	
								});
							} else {
								logger.error("could not make session");
							}
						});
					}//}	
				});
				connection.close();
			}
		});
	}
	
	private void bookHandler(RoutingContext context) {
		// how the do i send the authorization to this thing
		String authorization = context.request().getHeader("Authorization");
		String auth2 = authorization.substring(7);
		
		dbClient.getConnection(ar -> {
			if(ar.failed()) {
				logger.error("Could not open a database connection", ar.cause());
			} else {
				logger.info("Verifing session "+auth2+" from database...");
				SQLConnection connection = ar.result();
				JsonArray params = new JsonArray().add(auth2);
				
				connection.queryWithParams(SQL_VERIFY_SESSION,
					params, result -> {
						List<JsonArray> rows = result.result().getResults();
						if(rows.size() < 1) {
							connection.close();
							context.response().putHeader("Content-Type", "text/html");
							context.response().setStatusCode(401);
							context.fail(401);
							
							logger.error("Invalid session credentials");
							//context.fail(401);
							context.response().end();
						} else {
							connection.query("Select now()", result2 ->{
								String now = result2.result().getResults().get(0).getString(0);
								String then = result.result().getResults().get(0).getString(3);
								now = now.replace('T', ' ');
								then = then.replace('T', ' ');
								Timestamp nowTime, thenTime;
								nowTime = Timestamp.valueOf(now);
								thenTime = Timestamp.valueOf(then);
								long difference = nowTime.getTime() - thenTime.getTime();
								difference /= 60000;
								
								if (difference >= 1) {
									connection.close();
									logger.error("Session expired");
									context.response().putHeader("Content-Type", "text/html");
									context.response().setStatusCode(401);
									context.fail(401);
									context.response().end();	
								} else {
									connection.querySingleWithParams(SQL_VERIFY_PERMISSION, 
										new JsonArray().add(result.result().getResults().get(0).getInteger(1)),
										result3 ->{
											String permission = result3.result().getString(2);
											int allowed = result3.result().getInteger(3);
											if ((permission.compareTo("book report") == 0) && (allowed == 1)) {
												logger.info("access granted");
												
												connection.query(SQL_GET_BOOKS, results4 -> {
													logger.info("pulling book data");
													List<JsonArray> rows2 = results4.result().getResults();
													if(rows2.size() < 1) {
														connection.close();
														logger.error("Unable to pull Book and Publisher data");
														context.response().putHeader("Content-Type", "text/html");
														context.response().setStatusCode(401);
														context.fail(401);
														context.response().end();
													} else {
														logger.info("attempting to make spreadsheet");
														
														makeSpreadsheet(rows2);
														logger.info("spreadsheet saved");
														context.response().putHeader("Content-Type", "application/vnd.ms-excel");
														context.response().putHeader("Content-Disposition", "attachment; filename=book_report.xls");
														context.response().sendFile("./book_report.xls");
														logger.info("file sent");
														context.response().end();
														connection.close();
													}
												});
											} else {
												connection.close();
												logger.error("Invalid access");
												context.response().putHeader("Content-Type", "text/html");
												context.response().setStatusCode(401);
												context.fail(401);
												context.response().end();
											}
									});
								}
							});
						}
				});
			}
		});
	}
	
	private void makeSpreadsheet(List<JsonArray> rows2) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Books");
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontName("Courier New");
		titleFont.setFontHeightInPoints((short) 24);
		titleFont.setBold(true);
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Publishers and Books");
		cell.setCellStyle(titleStyle);
		int rowNum = 2;
		
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle boldStyle = workbook.createCellStyle();
		boldStyle.setFont(font);
		
		row = sheet.createRow(rowNum++);
		
		int cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Book Title");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Publisher");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Year Published");
		cell.setCellStyle(boldStyle);
		
		
		row = sheet.createRow(rowNum++);
		//add cells 0 to 3
		int i = 0;
		for(i = 0; i < rows2.size(); i++) {
			//JsonArray rowEntry: rows2
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(rows2.get(i).getString(0));
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(rows2.get(i).getString(1));
			
			cell = row.createCell(cellNum++);
			cell.setCellValue(rows2.get(i).getInteger(2));
		}
		//summary info
		//skip a row and add an average age formula
		int oldRN = rowNum;
		rowNum = rowNum++;
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellValue("Total Books");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(2);
		cell.setCellFormula("COUNT(C4:C" + oldRN + ")");
		cell.setCellStyle(boldStyle);
		
		rowNum = rowNum+2;
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellValue("Total Publishers");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(2);
		cell.setCellFormula("SUM(1/COUNTIF(B4:B" + (oldRN) + ",B1:B"+(oldRN)+"))");
		cell.setCellStyle(boldStyle);
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		
		try (FileOutputStream f = new FileOutputStream(new File(DEST_FILE_PATH))) {
			workbook.write(f);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("book spreadsheet has been created");
	}

	

}
