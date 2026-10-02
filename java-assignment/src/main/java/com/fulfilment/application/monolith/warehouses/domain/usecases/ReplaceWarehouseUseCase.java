package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }


  @Override
  public void replace(Warehouse newWarehouse) {
    Warehouse current = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);

    if (current == null) {
      throw new IllegalArgumentException("Warehouse to replace does not exist.");
    }

    // 1. Stock must remain exactly the same
    if (!current.stock.equals(newWarehouse.stock)) {
      throw new IllegalArgumentException("New warehouse stock must match existing warehouse stock.");
    }

    // 2. New capacity must accommodate existing stock
    if (newWarehouse.capacity == null || newWarehouse.capacity < current.stock) {
      throw new IllegalArgumentException("New warehouse capacity cannot accommodate existing stock.");
    }

    // 3. New location must exist
    var location = locationResolver.resolveByIdentifier(newWarehouse.location);

    if (location == null) {
      throw new IllegalArgumentException("Location does not exist.");
    }

    // 4. Stock cannot exceed capacity
    if (newWarehouse.stock > newWarehouse.capacity) {
      throw new IllegalArgumentException("Warehouse capacity cannot accommodate the stock.");
    }

    // 5. Check location warehouse limit
    var otherWarehouses = warehouseStore.getAll().stream()
            .filter(w -> !w.businessUnitCode.equals(current.businessUnitCode))
            .filter(w -> newWarehouse.location.equals(w.location))
            .toList();

    if (otherWarehouses.size() >= location.maxNumberOfWarehouses) {
       throw new IllegalArgumentException("Maximum number of warehouses reached for location.");
    }

    // 6. Check location total capacity
    int usedCapacity = otherWarehouses.stream().mapToInt(w -> w.capacity).sum();

    if (usedCapacity + newWarehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Warehouse capacity exceeds location capacity.");
    }

    // 7. Archive old warehouse
    current.archivedAt = LocalDateTime.now();
    warehouseStore.update(current);

    // 8. Create replacement
    newWarehouse.createdAt = LocalDateTime.now();
    newWarehouse.archivedAt = null;

    warehouseStore.create(newWarehouse);
  }
}
