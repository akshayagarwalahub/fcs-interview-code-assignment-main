package com.fulfilment.application.monolith.stores;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;

@ApplicationScoped
public class StoreEventHandler {

    private final LegacyStoreManagerGateway legacyStoreManagerGateway;

    public StoreEventHandler(
            LegacyStoreManagerGateway legacyStoreManagerGateway) {
        this.legacyStoreManagerGateway = legacyStoreManagerGateway;
    }

    public void handleStoreChanged(
            @Observes(during = TransactionPhase.AFTER_SUCCESS)
            StoreChangedEvent event) {

        if (event.operation() == StoreChangedEvent.Operation.CREATE) {
            legacyStoreManagerGateway
                    .createStoreOnLegacySystem(event.store());
        }

        if (event.operation() == StoreChangedEvent.Operation.UPDATE) {
            legacyStoreManagerGateway
                    .updateStoreOnLegacySystem(event.store());
        }
    }
}