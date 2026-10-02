package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import jakarta.persistence.*;

@Entity
@Table(
        name = "fulfilment",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "product_id",
                        "store_id",
                        "warehouse_business_unit_code"
                }
        )
)
public class Fulfilment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    public Store store;

    @Column(name = "warehouse_business_unit_code", nullable = false)
    public String warehouseBusinessUnitCode;

    public Fulfilment() {
    }

    public Fulfilment(
            Product product,
            Store store,
            String warehouseBusinessUnitCode) {

        this.product = product;
        this.store = store;
        this.warehouseBusinessUnitCode = warehouseBusinessUnitCode;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Store getStore() {
        return store;
    }

    public String getWarehouseBusinessUnitCode() {
        return warehouseBusinessUnitCode;
    }
}