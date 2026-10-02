package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

    @Inject
    private WarehouseRepository warehouseRepository;

    @Inject
    CreateWarehouseOperation createWarehouseOperation;

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @Inject
    ReplaceWarehouseOperation replaceWarehouseOperation;

    @Override
    public List<Warehouse> listAllWarehousesUnits() {
        return warehouseRepository.getAll().stream().map(this::toWarehouseResponse)
                .toList();
    }

    @Override
    @Transactional
    public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
        var warehouse = toDomain(data);
        createWarehouseOperation.create(warehouse);
        return toWarehouseResponse(warehouse);
    }

    @Override
    public Warehouse getAWarehouseUnitByID(String id) {
        try {
            Long warehouseId = Long.valueOf(id);
            var warehouse = warehouseRepository.findWarehouseById(warehouseId);

            if (warehouse == null) {
                throw new WebApplicationException("Warehouse not found.", 404);
            }

            return toWarehouseResponse(warehouse);
        } catch (NumberFormatException e) {
            throw new WebApplicationException(
                    "Invalid warehouse id.", 400);
        }
    }

    @Override
    @Transactional
    public void archiveAWarehouseUnitByID(String id) {
        try {
            Long warehouseId = Long.valueOf(id);
            var warehouse = warehouseRepository.findWarehouseById(warehouseId);
            if (warehouse == null) {
                throw new WebApplicationException("Warehouse not found.", 404);
            }
            archiveWarehouseOperation.archive(warehouse);
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid warehouse id.", 400);
        }
    }

    @Override
    @Transactional
    public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
        var warehouse = toDomain(data);
        warehouse.businessUnitCode = businessUnitCode;
        replaceWarehouseOperation.replace(warehouse);
        return toWarehouseResponse(warehouse);
    }

    /**
     * Converts domain Warehouse to API Warehouse.
     */
    private Warehouse toWarehouseResponse(com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
        var response = new Warehouse();

        response.setBusinessUnitCode(warehouse.businessUnitCode);
        response.setLocation(warehouse.location);
        response.setCapacity(warehouse.capacity);
        response.setStock(warehouse.stock);
        return response;
    }

    /**
     * Converts API Warehouse to domain Warehouse.
     */
    private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomain(Warehouse data) {
        var warehouse = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        warehouse.businessUnitCode = data.getBusinessUnitCode();
        warehouse.location = data.getLocation();
        warehouse.capacity = data.getCapacity();
        warehouse.stock = data.getStock();
        return warehouse;
    }
}