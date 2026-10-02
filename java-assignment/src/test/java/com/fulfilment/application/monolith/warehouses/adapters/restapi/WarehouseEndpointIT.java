package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  public void testListAllWarehouses() {
    given()
            .when()
            .get("/warehouse")
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("MWH.012"),
                    containsString("MWH.023"));
  }

  @Test
  public void testGetWarehouseById() {
    given()
            .when()
            .get("/warehouse/1")
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("ZWOLLE-001"));
  }

  @Test
  public void testGetWarehouseWithInvalidId() {
    given()
            .when()
            .get("/warehouse/invalid")
            .then()
            .statusCode(400);
  }

  @Test
  public void testGetNonExistingWarehouse() {
    given()
            .when()
            .get("/warehouse/99999")
            .then()
            .statusCode(404);
  }

  @Test
  public void testCreateWarehouse() {
    String request =
            """
            {
              "businessUnitCode": "MWH.TEST",
              "location": "AMSTERDAM-001",
              "capacity": 100,
              "stock": 50
            }
            """;

    given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(201)
            .body(
                    containsString("MWH.TEST"),
                    containsString("AMSTERDAM-001"));
  }

  @Test
  public void testCreateWarehouseWithInvalidLocation() {
    String request =
            """
            {
              "businessUnitCode": "MWH.INVALID.LOCATION",
              "location": "INVALID-LOCATION",
              "capacity": 100,
              "stock": 50
            }
            """;

    given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  public void testCreateWarehouseWithStockGreaterThanCapacity() {
    String request =
            """
            {
              "businessUnitCode": "MWH.INVALID.STOCK",
              "location": "AMSTERDAM-001",
              "capacity": 50,
              "stock": 100
            }
            """;

    given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  public void testCreateWarehouseWithDuplicateBusinessUnitCode() {
    String request =
            """
            {
              "businessUnitCode": "MWH.001",
              "location": "AMSTERDAM-001",
              "capacity": 100,
              "stock": 50
            }
            """;

    given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  public void testArchiveWarehouse() {

    // Archive existing warehouse
    given()
            .when()
            .delete("/warehouse/1")
            .then()
            .statusCode(204);

    // It should no longer be returned by the list endpoint
    given()
            .when()
            .get("/warehouse")
            .then()
            .statusCode(200)
            .body(not(containsString("MWH.001")));
  }

  @Test
  public void testArchiveNonExistingWarehouse() {
    given()
            .when()
            .delete("/warehouse/99999")
            .then()
            .statusCode(404);
  }

  @Test
  public void testArchiveWarehouseWithInvalidId() {
    given()
            .when()
            .delete("/warehouse/invalid")
            .then()
            .statusCode(400);
  }
}