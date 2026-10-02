package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateWarehouseUseCaseTest {

    @Test
    void shouldCreateWarehouseSuccessfully() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 100;
        warehouse.stock = 50;

        useCase.create(warehouse);

        assertEquals(1, warehouseStore.warehouses.size());
        assertEquals(
                "MWH.001",
                warehouseStore.warehouses.get(0).businessUnitCode
        );
        assertEquals(
                "AMSTERDAM-001",
                warehouseStore.warehouses.get(0).location
        );
        assertEquals(100, warehouseStore.warehouses.get(0).capacity);
        assertEquals(50, warehouseStore.warehouses.get(0).stock);

        assertNotNull(warehouse.createdAt);
    }


    @Test
    void shouldRejectDuplicateBusinessUnitCode() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.businessUnitCode = "MWH.001";
        existingWarehouse.location = "AMSTERDAM-001";
        existingWarehouse.capacity = 100;
        existingWarehouse.stock = 50;

        warehouseStore.warehouses.add(existingWarehouse);

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 100;
        warehouse.stock = 50;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Business unit code already exists.",
                exception.getMessage()
        );

        assertEquals(1, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectInvalidLocation() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        // No location configured
        FakeLocationResolver locationResolver =
                new FakeLocationResolver(null);

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "INVALID";
        warehouse.capacity = 100;
        warehouse.stock = 50;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Location does not exist.",
                exception.getMessage()
        );

        assertEquals(0, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectZeroCapacity() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 0;
        warehouse.stock = 0;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Warehouse capacity must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectNegativeCapacity() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = -10;
        warehouse.stock = 0;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Warehouse capacity must be greater than zero.",
                exception.getMessage()
        );

        assertEquals(0, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectNegativeStock() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 100;
        warehouse.stock = -1;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Warehouse stock cannot be negative.",
                exception.getMessage()
        );

        assertEquals(0, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectStockGreaterThanCapacity() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 3, 1000)
                );

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "MWH.001";
        warehouse.location = "AMSTERDAM-001";
        warehouse.capacity = 50;
        warehouse.stock = 100;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse)
                );

        assertEquals(
                "Warehouse capacity cannot accommodate the stock.",
                exception.getMessage()
        );

        assertEquals(0, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectWhenMaximumWarehousesReached() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 2, 1000)
                );

        Warehouse warehouse1 = new Warehouse();
        warehouse1.businessUnitCode = "MWH.001";
        warehouse1.location = "AMSTERDAM-001";
        warehouse1.capacity = 100;
        warehouse1.stock = 50;

        Warehouse warehouse2 = new Warehouse();
        warehouse2.businessUnitCode = "MWH.002";
        warehouse2.location = "AMSTERDAM-001";
        warehouse2.capacity = 100;
        warehouse2.stock = 50;

        warehouseStore.warehouses.add(warehouse1);
        warehouseStore.warehouses.add(warehouse2);

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse warehouse3 = new Warehouse();
        warehouse3.businessUnitCode = "MWH.003";
        warehouse3.location = "AMSTERDAM-001";
        warehouse3.capacity = 100;
        warehouse3.stock = 50;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(warehouse3)
                );

        assertEquals(
                "Maximum number of warehouses reached for location.",
                exception.getMessage()
        );

        assertEquals(2, warehouseStore.warehouses.size());
    }


    @Test
    void shouldRejectWhenLocationCapacityExceeded() {

        FakeWarehouseStore warehouseStore = new FakeWarehouseStore();

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 5, 200)
                );

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.businessUnitCode = "MWH.001";
        existingWarehouse.location = "AMSTERDAM-001";
        existingWarehouse.capacity = 150;
        existingWarehouse.stock = 100;

        warehouseStore.warehouses.add(existingWarehouse);

        CreateWarehouseUseCase useCase =
                new CreateWarehouseUseCase(
                        warehouseStore,
                        locationResolver
                );

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "MWH.002";
        newWarehouse.location = "AMSTERDAM-001";
        newWarehouse.capacity = 100;
        newWarehouse.stock = 50;

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(newWarehouse)
                );

        assertEquals(
                "Warehouse capacity exceeds location capacity.",
                exception.getMessage()
        );

        assertEquals(1, warehouseStore.warehouses.size());
    }


    /*
     * ---------------------------------------------------------
     * Fake WarehouseStore
     * ---------------------------------------------------------
     */

    private static class FakeWarehouseStore implements WarehouseStore {

        private final List<Warehouse> warehouses = new ArrayList<>();

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
            // Not required for CreateWarehouseUseCase tests
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


    /*
     * ---------------------------------------------------------
     * Fake LocationResolver
     * ---------------------------------------------------------
     */

    private static class FakeLocationResolver
            implements LocationResolver {

        private final Location location;

        private FakeLocationResolver(Location location) {
            this.location = location;
        }

        @Override
        public Location resolveByIdentifier(String identifier) {

            if (location == null) {
                return null;
            }

            if (location.identification.equals(identifier)) {
                return location;
            }

            return null;
        }
    }
}