package cz.basicland.metrics.impl;

import dev.cubxity.plugins.metrics.api.metric.collector.Collector;
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection;
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric;
import dev.cubxity.plugins.metrics.api.metric.data.Metric;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MobCaps implements Collector, CollectorCollection {
    private final static String METRIC_NAME = "minecraft_world_mob_caps";

    @NotNull
    @Override
    public List<Metric> collect() {
        List<Metric> samples = new ArrayList<>();
        for (ServerLevel world : DedicatedServer.getServer().getAllLevels()) {
            WorldData parse = parse(world);
            String name = world.serverLevelData.getLevelName();

            samples.add(new GaugeMetric(METRIC_NAME + "_spawnable_chunks", Map.of("world", name), parse.spawnableChunks()));
            for (Data data : parse.data()) {
                Map<String, String> mobLabels = Map.of(
                        "mob_category", data.category().getName(),
                        "world", name
                );
                samples.add(new GaugeMetric(METRIC_NAME + "_current", mobLabels, data.current()));
                samples.add(new GaugeMetric(METRIC_NAME + "_limit", mobLabels, data.limit()));
            }
        }
        return samples;
    }

    private WorldData parse(ServerLevel level) {
        final NaturalSpawner.SpawnState state = level.getChunkSource().getLastSpawnState();

        final int chunks;
        if (state == null) {
            chunks = 0;
        } else {
            chunks = state.getSpawnableChunkCount();
        }

        List<Data> list = new ArrayList<>();

        for (MobCategory category : MobCategory.values()) {
            int current = state == null ? 0 : state.getMobCategoryCounts().getOrDefault(category, 0);

            int limit = NaturalSpawner.globalLimitForCategory(level, category, chunks);
            list.add(new Data(category, current, limit));
        }

        return new WorldData(list, chunks);
    }

    @Override
    public @NotNull List<Collector> getCollectors() {
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

    private record WorldData(List<Data> data, int spawnableChunks) {}
    private record Data(MobCategory category, int current, int limit) {}
}
