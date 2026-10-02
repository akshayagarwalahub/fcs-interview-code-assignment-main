package com.fulfilment.application.monolith.fulfilment;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FulfilmentEndpointTest {

    private static final String PATH = "/fulfilment";

    @Test
    public void testCreateFulfilment() {

        createFulfilment(
                1,
                1,
                "MWH.001",
                201);
    }

    @Test
    public void testInvalidProductIsRejected() {

        createFulfilment(
                99999,
                1,
                "MWH.001",
                400);
    }

    @Test
    public void testInvalidStoreIsRejected() {

        createFulfilment(
                1,
                99999,
                "MWH.001",
                400);
    }

    @Test
    public void testInvalidWarehouseIsRejected() {

        createFulfilment(
                1,
                1,
                "INVALID-WAREHOUSE",
                400);
    }

    @Test
    public void testProductCanHaveMaximumTwoWarehousesPerStore() {

        /*
         * Product 2 + Store 1
         *
         * Warehouse 1 -> allowed
         * Warehouse 2 -> allowed
         * Warehouse 3 -> rejected
         */

        createFulfilment(
                2,
                1,
                "MWH.001",
                201);

        createFulfilment(
                2,
                1,
                "MWH.012",
                201);

        createFulfilment(
                2,
                1,
                "MWH.023",
                400);
    }

    @Test
    public void testStoreCanHaveMaximumThreeWarehouses() {

        /*
         * Store 2
         *
         * Warehouse 1 -> Product 1
         * Warehouse 2 -> Product 2
         * Warehouse 3 -> Product 3
         *
         * These are three different warehouses.
         *
         * A fourth different warehouse is impossible because
         * the supplied database only has three warehouses.
         *
         * So we test that all three can be assigned.
         */

        createFulfilment(
                1,
                2,
                "MWH.001",
                201);

        createFulfilment(
                2,
                2,
                "MWH.012",
                201);

        createFulfilment(
                3,
                2,
                "MWH.023",
                201);
    }

    @Test
    public void testWarehouseCanStoreDifferentProducts() {

        /*
         * MWH.023 can be associated with different products.
         *
         * The supplied import.sql only contains 3 products,
         * so we cannot genuinely test the "maximum 5 products"
         * boundary without adding more products.
         */

        createFulfilment(
                1,
                1,
                "MWH.023",
                201);

        createFulfilment(
                2,
                2,
                "MWH.023",
                201);

        createFulfilment(
                3,
                3,
                "MWH.023",
                201);
    }

    @Test
    public void testDuplicateFulfilmentIsRejected() {

        /*
         * Use a combination that is different from the other tests.
         */
        createFulfilment(
                3,
                1,
                "MWH.001",
                201);

        createFulfilment(
                3,
                1,
                "MWH.001",
                400);
    }

    private void createFulfilment(
            long productId,
            long storeId,
            String warehouseBusinessUnitCode,
            int expectedStatus) {

        String request =
                """
                {
                  "productId": %d,
                  "storeId": %d,
                  "warehouseBusinessUnitCode": "%s"
                }
                """.formatted(
                        productId,
                        storeId,
                        warehouseBusinessUnitCode);

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH)
                .then()
                .statusCode(expectedStatus);
    }
}