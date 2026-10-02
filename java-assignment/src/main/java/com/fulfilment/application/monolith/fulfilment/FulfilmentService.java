package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FulfilmentService {

    private final FulfilmentRepository fulfilmentRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public FulfilmentService(
            FulfilmentRepository fulfilmentRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository) {

        this.fulfilmentRepository = fulfilmentRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public Fulfilment associate(
            Long productId,
            Long storeId,
            String warehouseBusinessUnitCode) {

        // 1. Find product
        Product product = productRepository.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        // 2. Find store
        Store store = Store.findById(storeId);

        if (store == null) {
            throw new IllegalArgumentException("Store not found.");
        }

        // 3. Find warehouse
        var warehouse =
                warehouseRepository.findByBusinessUnitCode(
                        warehouseBusinessUnitCode);

        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse not found.");
        }

        // 4. Prevent exact duplicate
        if (fulfilmentRepository.countByProductStoreWarehouse(
                productId,
                storeId,
                warehouseBusinessUnitCode) > 0) {

            throw new IllegalArgumentException(
                    "This fulfilment already exists.");
        }

        // 5. Product can have maximum 2 warehouses for one store
        long productWarehouseCount =
                fulfilmentRepository.countByProductAndStore(
                        productId,
                        storeId);

        if (productWarehouseCount >= 2) {
            throw new IllegalArgumentException(
                    "A product can be fulfilled by maximum 2 warehouses per store.");
        }

        // 6. Store can have maximum 3 different warehouses
        long storeWarehouseCount =
                fulfilmentRepository.countDistinctWarehousesByStore(
                        storeId);

        boolean warehouseAlreadyAssignedToStore =
                fulfilmentRepository.countByStoreAndWarehouse(
                        storeId,
                        warehouseBusinessUnitCode) > 0;

        if (storeWarehouseCount >= 3
                && !warehouseAlreadyAssignedToStore) {

            throw new IllegalArgumentException(
                    "A store can be fulfilled by maximum 3 different warehouses.");
        }

        // 7. Warehouse can store maximum 5 different products
        long warehouseProductCount =
                fulfilmentRepository.countDistinctProductsByWarehouse(
                        warehouseBusinessUnitCode);

        boolean productAlreadyAssignedToWarehouse =
                fulfilmentRepository.countByWarehouseAndProduct(
                        warehouseBusinessUnitCode,
                        productId) > 0;

        if (warehouseProductCount >= 5
                && !productAlreadyAssignedToWarehouse) {

            throw new IllegalArgumentException(
                    "A warehouse can store maximum 5 different products.");
        }

        // 8. Create fulfilment association
        Fulfilment fulfilment = new Fulfilment();

        fulfilment.product = product;
        fulfilment.store = store;
        fulfilment.warehouseBusinessUnitCode =
                warehouseBusinessUnitCode;

        fulfilmentRepository.persist(fulfilment);

        return fulfilment;
    }
}