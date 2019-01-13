package ru.hutoroff.interview.revolut;

import org.jooby.Err;
import org.jooby.Request;
import org.jooby.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hutoroff.interview.revolut.data.exception.StorageException;
import ru.hutoroff.interview.revolut.service.exception.BusinessException;

public class ErrorHandler implements Err.Handler {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void handle(Request req, Response rsp, Err ex) throws Throwable {
        if (ex.getCause() instanceof BusinessException) {
            LOG.error("Business error on processing request [{}] {}", req.method(), req.path(), ex);
        }
        if (ex.getCause() instanceof StorageException) {
            LOG.error("Failed to work with storage for request [{}] {}", req.method(), req.path(), ex);
        }
    }
}
