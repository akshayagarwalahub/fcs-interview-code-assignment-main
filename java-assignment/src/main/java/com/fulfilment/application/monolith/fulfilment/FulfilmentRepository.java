package com.fulfilment.application.monolith.fulfilment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FulfilmentRepository implements PanacheRepository<Fulfilment> {

    /**
     * Checks whether the exact Product + Store + Warehouse
     * association already exists.
     */
    public long countByProductStoreWarehouse(
            Long productId,
            Long storeId,
            String warehouseBusinessUnitCode) {

        return count(
                "product.id = ?1 and store.id = ?2 and warehouseBusinessUnitCode = ?3",
                productId,
                storeId,
                warehouseBusinessUnitCode);
    }

    /**
     * Number of different warehouses fulfilling a Product
     * for a particular Store.
     */
    public long countByProductAndStore(
            Long productId,
            Long storeId) {

        return find(
                "product.id = ?1 and store.id = ?2",
                productId,
                storeId)
                .stream()
                .map(Fulfilment::getWarehouseBusinessUnitCode)
                .distinct()
                .count();
    }

    /**
     * Number of different warehouses fulfilling a Store.
     */
    public long countDistinctWarehousesByStore(Long storeId) {

        return find("store.id = ?1", storeId)
                .stream()
                .map(Fulfilment::getWarehouseBusinessUnitCode)
                .distinct()
                .count();
    }

    /**
     * Checks whether a particular Warehouse is already
     * associated with a Store.
     */
    public long countByStoreAndWarehouse(
            Long storeId,
            String warehouseBusinessUnitCode) {

        return count(
                "store.id = ?1 and warehouseBusinessUnitCode = ?2",
                storeId,
                warehouseBusinessUnitCode);
    }

    /**
     * Number of different Products stored by a Warehouse.
     */
    public long countDistinctProductsByWarehouse(
            String warehouseBusinessUnitCode) {

        return find(
                "warehouseBusinessUnitCode = ?1",
                warehouseBusinessUnitCode)
                .stream()
                .map(f -> f.getProduct().id)
                .distinct()
                .count();
    }

    /**
     * Checks whether a particular Product is already
     * stored in a particular Warehouse.
     */
    public long countByWarehouseAndProduct(
            String warehouseBusinessUnitCode,
            Long productId) {

        return count(
                "warehouseBusinessUnitCode = ?1 and product.id = ?2",
                warehouseBusinessUnitCode,
                productId);
    }
}