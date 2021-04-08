package com.transfer;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public interface SqlReader {
    void loadResource(Resource resource, ApplicationContext applicationContext);
}
