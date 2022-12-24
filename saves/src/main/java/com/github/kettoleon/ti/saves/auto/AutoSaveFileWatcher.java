package com.github.kettoleon.ti.saves.auto;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
public class AutoSaveFileWatcher implements Runnable {

    public static final String AUTOSAVE_FILE_NAME = "Autosave.gz";
    public static final String EXITSAVE_FILE_NAME = "Exitsave.gz";
    private final WatchService watcher;
    private Path savesFolder;
    private Consumer<Path> onNewAutoSave;

    public AutoSaveFileWatcher(Path savesFolder, Consumer<Path> onNewAutoSave) {

        this.savesFolder = savesFolder;
        this.onNewAutoSave = onNewAutoSave;

        try {
            watcher = FileSystems.getDefault().newWatchService();
            savesFolder.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        //TODO uncompressed files support?
        Path autoSaveFile = Path.of(savesFolder.toString(), AUTOSAVE_FILE_NAME);
//        Path exitSaveFile = Path.of(savesFolder.toString(), EXITSAVE_FILE_NAME);
        //TODO don't like exitsave since it is there if you start a new game, maybe go for last modified file instead?
//        if (Files.exists(exitSaveFile)) {
//            log.info("Event: {} {}", "ALREADY_EXISTS", exitSaveFile.getFileName());
//            triggerUpdate(exitSaveFile);
//        } else
            if (Files.exists(autoSaveFile)) {
            log.info("Event: {} {}", "ALREADY_EXISTS", autoSaveFile.getFileName());
            triggerUpdate(autoSaveFile);
        }
        try {
            WatchKey key;
            while ((key = watcher.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    log.debug("Event: {} {}", event.kind().name(), event.context().toString());
                    if (event.kind().equals(ENTRY_MODIFY) && event.context().toString().equals(AUTOSAVE_FILE_NAME)) {
                        log.info("Event: {} {}", event.kind().name(), event.context().toString());
                        triggerUpdate(autoSaveFile);
                    } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY) && !event.context().toString().contains("Autosave") && !event.context().toString().contains("Exitsave") && event.context().toString().contains(".gz")) {
                        log.info("Event: {} {}", event.kind().name(), event.context().toString());
                        Path newSave = Path.of(savesFolder.toString(), event.context().toString());
                        triggerUpdate(newSave);
                    }
                }
                key.reset();
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void triggerUpdate(Path exitSaveFile) {
        try {
            log.debug("Processing savefile {} ...", exitSaveFile);
            onNewAutoSave.accept(exitSaveFile);
        } catch (Exception e) {
            log.error("Error processing savefile {}", exitSaveFile, e);
        }
    }


}
