package com.twilight.twilight.domain.bulletin.search.coordinator;

import com.twilight.twilight.domain.bulletin.search.handler.SearchSyncHandler;
import com.twilight.twilight.global.outbox.Type.AggregateType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SearchSyncHandlerRegistry {

    private final Map<AggregateType, SearchSyncHandler> handlerMap;

    public SearchSyncHandlerRegistry(List<SearchSyncHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        SearchSyncHandler::supports,
                        Function.identity()
                ));
    }

    public SearchSyncHandler get(AggregateType type) {
        SearchSyncHandler handler = handlerMap.get(type);
        if (handler == null) {
            throw new IllegalStateException(
                    "No SearchSyncHandler for type: " + type
            );
        }
        return handler;
    }
}
