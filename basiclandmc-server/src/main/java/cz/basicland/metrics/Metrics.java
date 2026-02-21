package cz.basicland.metrics;

import com.mojang.logging.LogUtils;
import cz.basicland.metrics.impl.MobCaps;
import dev.cubxity.plugins.metrics.api.UnifiedMetrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.slf4j.Logger;

public class Metrics {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init() {
        RegisteredServiceProvider<UnifiedMetrics> registration = Bukkit.getServer().getServicesManager().getRegistration(UnifiedMetrics.class);

        if (registration != null) {
            UnifiedMetrics unifiedMetrics = registration.getProvider();
            unifiedMetrics.getMetricsManager().registerCollection(new MobCaps());
            LOGGER.info("UnifiedMetrics hook has been initialized");
            return;
        }
        LOGGER.warn("UnifiedMetrics plugin not found, metrics will be disabled");
    }
}
