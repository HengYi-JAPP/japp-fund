package com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class BalanceSumTypeConfigWatchTask {
    private static final Logger log = LoggerFactory.getLogger(BalanceSumTypeConfigWatchTask.class);
    private final Path dir;
    private final String fileName;
    private boolean watched;

    public BalanceSumTypeConfigWatchTask(Path dir, String fileName) {
        this.dir = dir;
        this.fileName = fileName;
    }

    public BalanceSumTypeConfigWatchTask(String dir, String fileName) {
        this(Paths.get(dir), fileName);
    }

    public synchronized void watch(Consumer<JsonNode> consumer) {
        if (watched) {
            return;
        }
        watched = true;
        getJsonNode(dir.resolve(fileName)).ifPresent(consumer);

        Executors.newSingleThreadExecutor().submit(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                dir.register(watcher, ENTRY_MODIFY);

                while (true) {
                    WatchKey watchKey = watcher.take();

                    watchKey.pollEvents()
                            .stream()
                            .filter(it -> it.kind() == ENTRY_MODIFY)
                            .map(WatchEvent::context)
                            .map(it -> (Path) it)
                            .filter(it -> it.endsWith(fileName))
                            .map(dir::resolve)
                            .map(this::getJsonNode)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(consumer);

                    boolean valid = watchKey.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private Optional<JsonNode> getJsonNode(Path path) {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            final JsonNode node = yamlMapper.readTree(path.toFile());
            return Optional.of(node);
        } catch (Exception e) {
            log.error("", e);
        }
        return Optional.empty();
    }
}
