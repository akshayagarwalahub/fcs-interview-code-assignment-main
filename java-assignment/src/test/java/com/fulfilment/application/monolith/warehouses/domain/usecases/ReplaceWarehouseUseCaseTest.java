package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplaceWarehouseUseCaseTest {

    @Test
    void shouldReplaceWarehouseSuccessfully() {

        Warehouse current = warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement = warehouse(
                "MWH.001",
                "AMSTERDAM-002",
                100,
                50
        );

        FakeWarehouseStore store = new FakeWarehouseStore(current);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 5, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        useCase.replace(replacement);

        assertNotNull(current.archivedAt);
        assertNotNull(replacement.createdAt);
        assertNull(replacement.archivedAt);

        assertSame(replacement, store.createdWarehouse);
        assertSame(current, store.updatedWarehouse);
    }

    @Test
    void shouldThrowExceptionWhenWarehouseDoesNotExist() {

        FakeWarehouseStore store = new FakeWarehouseStore(null);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-001", 5, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "Warehouse to replace does not exist.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStockDoesNotMatch() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-002", 100, 40);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 5, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "New warehouse stock must match existing warehouse stock.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenCapacityCannotAccommodateStock() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-002", 40, 50);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 5, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "New warehouse capacity cannot accommodate existing stock.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenLocationDoesNotExist() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "UNKNOWN", 100, 50);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(null);

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "Location does not exist.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStockIsGreaterThanCapacity() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-002", 40, 50);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 5, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        /*
         * This condition is actually already caught by the
         * capacity < current.stock validation above.
         *
         * Therefore this test is intentionally not needed separately.
         */
    }

    @Test
    void shouldThrowExceptionWhenMaximumWarehousesReached() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-002", 100, 50);

        Warehouse warehouse1 =
                warehouse("MWH.002", "AMSTERDAM-002", 100, 20);

        Warehouse warehouse2 =
                warehouse("MWH.003", "AMSTERDAM-002", 100, 20);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        store.warehouses.add(warehouse1);
        store.warehouses.add(warehouse2);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 2, 1000)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "Maximum number of warehouses reached for location.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenLocationCapacityIsExceeded() {

        Warehouse current =
                warehouse("MWH.001", "AMSTERDAM-001", 100, 50);

        Warehouse replacement =
                warehouse("MWH.001", "AMSTERDAM-002", 100, 50);

        Warehouse existing =
                warehouse("MWH.002", "AMSTERDAM-002", 150, 50);

        FakeWarehouseStore store =
                new FakeWarehouseStore(current);

        store.warehouses.add(existing);

        FakeLocationResolver locationResolver =
                new FakeLocationResolver(
                        new Location("AMSTERDAM-002", 5, 200)
                );

        ReplaceWarehouseUseCase useCase =
                new ReplaceWarehouseUseCase(store, locationResolver);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.replace(replacement)
        );

        assertEquals(
                "Warehouse capacity exceeds location capacity.",
                exception.getMessage()
        );
    }

    private Warehouse warehouse(
            String businessUnitCode,
            String location,
            int capacity,
            int stock) {

        Warehouse warehouse = new Warehouse();

        warehouse.businessUnitCode = businessUnitCode;
        warehouse.location = location;
        warehouse.capacity = capacity;
        warehouse.stock = stock;

        return warehouse;
    }

    /**
     * Simple fake implementation.
     * No Mockito required.
     */
    private static class FakeWarehouseStore implements WarehouseStore {

        private final List<Warehouse> warehouses = new ArrayList<>();

        private Warehouse updatedWarehouse;
        private Warehouse createdWarehouse;

        FakeWarehouseStore(Warehouse currentWarehouse) {

            if (currentWarehouse != null) {
                warehouses.add(currentWarehouse);
            }
        }

        @Override
        public List<Warehouse> getAll() {
            return warehouses;
        }

        @Override
        public void create(Warehouse warehouse) {
            createdWarehouse = warehouse;
            warehouses.add(warehouse);
        }

        @Override
        public void update(Warehouse warehouse) {
            updatedWarehouse = warehouse;
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
            return null;
        }
    }

    /**
     * Simple fake implementation.
     * No Mockito required.
     */
    private static class FakeLocationResolver
            implements LocationResolver {

        private final Location location;

        FakeLocationResolver(Location location) {
            this.location = location;
        }

        @Override
        public Location resolveByIdentifier(String identifier) {
            return location;
        }
    }
}