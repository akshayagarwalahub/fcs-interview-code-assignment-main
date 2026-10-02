package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchiveWarehouseUseCaseTest {

    @Test
    void shouldArchiveWarehouse() {
        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();
        ArchiveWarehouseUseCase useCase =
                new ArchiveWarehouseUseCase(warehouseStore);

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 100;
        warehouse.stock = 50;

        useCase.archive(warehouse);

        assertNotNull(warehouse.archivedAt);
        assertSame(warehouse, warehouseStore.updatedWarehouse);
    }

    @Test
    void shouldUpdateWarehouseInStoreWhenArchived() {
        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();
        ArchiveWarehouseUseCase useCase =
                new ArchiveWarehouseUseCase(warehouseStore);

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.002";
        warehouse.location = "AMSTERDAM-002";
        warehouse.capacity = 200;
        warehouse.stock = 100;

        useCase.archive(warehouse);

        assertEquals("MWH.002",
                warehouseStore.updatedWarehouse.businessUnitCode);

        assertNotNull(warehouseStore.updatedWarehouse.archivedAt);
    }

    /**
     * Simple fake implementation.
     * No Mockito required.
     */
    private static class FakeWarehouseStore implements WarehouseStore {

        private final List<Warehouse> warehouses = new ArrayList<>();

        private Warehouse updatedWarehouse;

        @Override
        public List<Warehouse> getAll() {
            return warehouses;
        }

        @Override
        public void create(Warehouse warehouse) {
            warehouses.add(warehouse);
        }

        @Override
        public void update(Warehouse warehouse) {
            this.updatedWarehouse = warehouse;
        }

        @Override
        public void remove(Warehouse warehouse) {
            warehouses.remove(warehouse);
        }

        @Override
        public Warehouse findByBusinessUnitCode(String buCode) {
            return warehouses.stream()
                    .filter(w -> buCode.equals(w.businessUnitCode))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Warehouse findWarehouseById(Long id) {
            return warehouses.stream()
                    .filter(w -> id.equals(w.id))
                    .findFirst()
                    .orElse(null);
        }
    }
}