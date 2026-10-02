package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

@QuarkusTest
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class ProductEndpointTest {

  @Test
  @Order(1)
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
  @Order(2)
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
  @Order(3)
  public void testGetWarehouseWithInvalidId() {

    given()
            .when()
            .get("/warehouse/99999")
            .then()
            .statusCode(404);
  }

  @Test
  @Order(4)
  public void testGetWarehouseWithNonNumericId() {

    given()
            .when()
            .get("/warehouse/abc")
            .then()
            .statusCode(400);
  }

  @Test
  @Order(5)
  public void testCreateWarehouse() {

    String warehouse =
            """
            {
              "businessUnitCode": "MWH.TEST",
              "location": "ZWOLLE-002",
              "capacity": 40,
              "stock": 20
            }
            """;

    given()
            .contentType("application/json")
            .body(warehouse)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.TEST"),
                    containsString("ZWOLLE-002"));
  }

  @Test
  @Order(6)
  public void testCreateWarehouseWithDuplicateBusinessUnitCode() {

    String warehouse =
            """
            {
              "businessUnitCode": "MWH.001",
              "location": "AMSTERDAM-001",
              "capacity": 50,
              "stock": 10
            }
            """;

    given()
            .contentType("application/json")
            .body(warehouse)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  @Order(7)
  public void testCreateWarehouseWithInvalidLocation() {

    String warehouse =
            """
            {
              "businessUnitCode": "MWH.INVALID.LOCATION",
              "location": "INVALID-LOCATION",
              "capacity": 50,
              "stock": 10
            }
            """;

    given()
            .contentType("application/json")
            .body(warehouse)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  @Order(8)
  public void testCreateWarehouseWithStockGreaterThanCapacity() {

    String warehouse =
            """
            {
              "businessUnitCode": "MWH.INVALID.STOCK",
              "location": "AMSTERDAM-002",
              "capacity": 50,
              "stock": 100
            }
            """;

    given()
            .contentType("application/json")
            .body(warehouse)
            .when()
            .post("/warehouse")
            .then()
            .statusCode(400);
  }

  @Test
  @Order(9)
  public void testReplaceWarehouseWithMatchingStock() {

    String replacement =
            """
            {
              "location": "AMSTERDAM-001",
              "capacity": 40,
              "stock": 10
            }
            """;

    given()
            .contentType("application/json")
            .body(replacement)
            .when()
            .post("/warehouse/MWH.001/replacement")
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("AMSTERDAM-001"));
  }

  @Test
  @Order(10)
  public void testReplaceWarehouseWithMismatchedStock() {

    /*
     * Existing MWH.001 has stock = 10.
     * New warehouse has stock = 999.
     * Therefore replacement must be rejected.
     */
    String replacement =
            """
            {
              "location": "AMSTERDAM-001",
              "capacity": 150,
              "stock": 999
            }
            """;

    given()
            .contentType("application/json")
            .body(replacement)
            .when()
            .post("/warehouse/MWH.001/replacement")
            .then()
            .statusCode(400);
  }

  @Test
  @Order(11)
  public void testArchiveWarehouse() {

    /*
     * At this point MWH.001 has been replaced.
     * We therefore archive another existing warehouse.
     *
     * MWH.012 is expected to have ID 2 in the initial database.
     */
    given()
            .when()
            .delete("/warehouse/2")
            .then()
            .statusCode(204);

    given()
            .when()
            .get("/warehouse")
            .then()
            .statusCode(200)
            .body(
                    not(containsString("MWH.012")),
                    containsString("MWH.023"));
  }
}