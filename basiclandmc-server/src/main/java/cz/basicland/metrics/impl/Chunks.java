package cz.basicland.metrics.impl;

import dev.cubxity.plugins.metrics.api.metric.collector.Collector;
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection;
import dev.cubxity.plugins.metrics.api.metric.data.Metric;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;

public class Chunks implements Collector, CollectorCollection {
    @Override
    public @NonNull List<Metric> collect() {
        return List.of();
    }

    @Override
    public @NonNull List<Collector> getCollectors() {
        return Collections.singletonList(this);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void dispose() {

    }
}
