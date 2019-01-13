package ru.hutoroff.interview.revolut.controller;

import org.jooby.Request;
import org.jooby.Response;
import org.jooby.Status;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hutoroff.interview.revolut.controller.dto.Mapper;
import ru.hutoroff.interview.revolut.controller.dto.TransferRequest;
import ru.hutoroff.interview.revolut.service.TransferService;

import javax.inject.Inject;

@Path("/api/transfer")
public class TransferController {
    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;
    private final Mapper mapper;

    @Inject
    public TransferController(TransferService transferService, Mapper mapper) {
        this.transferService = transferService;
        this.mapper = mapper;
    }

    @POST
    public void doTransfer(Request request, Response response) throws Exception {
        TransferRequest transferRequest = mapper.toObject(request, TransferRequest.class);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Requested transfer from {} to {} of {}", transferRequest.from, transferRequest.to, transferRequest.amount);
        }

        if (transferRequest.from == null || transferRequest.to == null || transferRequest.amount == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        transferService.transferFromTo(transferRequest.from, transferRequest.to, transferRequest.amount);

        if (LOG.isTraceEnabled()) {
            LOG.trace("Transfer from {} to {} of {} succeed", transferRequest.from, transferRequest.to, transferRequest.amount);
        }
        response.status(Status.OK).end();
    }
}
