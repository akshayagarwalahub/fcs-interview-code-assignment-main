package com.fulfilment.application.monolith.fulfilment;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fulfilment")
@Consumes(MediaType.APPLICATION_JSON)
public class FulfilmentResource {

    @Inject
    FulfilmentService fulfilmentService;

    @POST
    public Response associate(FulfilmentRequest request) {

        fulfilmentService.associate(
                request.productId,
                request.storeId,
                request.warehouseBusinessUnitCode);

        return Response.status(Response.Status.CREATED).build();
    }
}