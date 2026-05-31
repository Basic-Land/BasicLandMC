package cz.basicland.metrics;

import com.mojang.logging.LogUtils;
import cz.basicland.metrics.impl.Chunks;
import cz.basicland.metrics.impl.MobCaps;
import dev.cubxity.plugins.metrics.api.UnifiedMetrics;
import dev.cubxity.plugins.metrics.api.metric.MetricsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.slf4j.Logger;

public class Metrics {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init() {
        RegisteredServiceProvider<UnifiedMetrics> registration = Bukkit.getServer().getServicesManager().getRegistration(UnifiedMetrics.class);

        if (registration != null) {
            MetricsManager metricsManager = registration.getProvider().getMetricsManager();
            metricsManager.registerCollection(new MobCaps());
            metricsManager.registerCollection(new Chunks());
            LOGGER.info("UnifiedMetrics hook has been initialized");
            return;
        }
        LOGGER.warn("UnifiedMetrics plugin not found, metrics will be disabled");
    }
}
