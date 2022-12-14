package com.github.kettoleon.ti.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.meta.model.MetaObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.github.kettoleon.ti.TiObjectMapper.TI_OBJECT_MAPPER;

public class MetaRepository {

    private final Map<String, Object> cache = new HashMap<>();

    private final Path metaFolder;

    public MetaRepository(Path metaFolder) {
        this.metaFolder = metaFolder;
    }

    public <T extends MetaObject> Optional<T> get(String templateName, Class<T> type) {
        if (StringUtils.isBlank(templateName)) return Optional.empty();

        String cacheKey = getCacheKey(type, templateName);
        if (!cache.containsKey(cacheKey)) {
            loadMetaFromFileIntoCache(templateName, type);
        }
        return Optional.ofNullable((T) cache.get(cacheKey));

    }

    public <T extends MetaObject> List<T> getAll(Class<T> type) {
        //TODO note this will ignore the cache
        List<T> all = new ArrayList<>();
        try {
            Path path = resolveFile(type);
            JsonNode root = TI_OBJECT_MAPPER.readTree(path.toFile());
            for (JsonNode template : root) {
                T metaObject = readMetaFromNodeAndSaveToCache(template, type);
                all.add(metaObject);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not get all metadata objects of class " + type.getSimpleName(), e);
        }
        return all;
    }

    private static <T extends MetaObject> String getCacheKey(Class<T> type, String templateName) {
        return type.getSimpleName() + "::" + templateName;
    }

    private <T extends MetaObject> void loadMetaFromFileIntoCache(String templateName, Class<T> type) {
        try {
            templateName = fixSomeTemplateNames(templateName);
            Path path = resolveFile(type);
            JsonNode root = TI_OBJECT_MAPPER.readTree(path.toFile());
            for (JsonNode template : root) {
                if (template.path("dataName").asText().equals(templateName)) {
                    readMetaFromNodeAndSaveToCache(template, type);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends MetaObject> T readMetaFromNodeAndSaveToCache(JsonNode template, Class<T> type) {
        try {
            T metaObject = TI_OBJECT_MAPPER.treeToValue(template, type);
            metaObject.setMetaRepository(this);
            cache.put(getCacheKey(type, metaObject.getDataName()), metaObject);
            return metaObject;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json parse issue while deserializing metadata object of type: " + type.getSimpleName(), e);
        }
    }

    private String fixSomeTemplateNames(String templateName) {
        //TODO are those codes really wrong? Should I send them a bug report? Or am I missing something?
        if (templateName.equals("GER")) {
            return "DEU";
        } else if (templateName.equals("FRA")) {
            return "EUA";
        }
        return templateName;
    }

    private Path resolveFile(Class type) {
        try {
            return Files.list(metaFolder).filter(f -> f.getFileName().toString().equals("TI" + type.getSimpleName() + "Template.json")).findFirst().orElseThrow(() -> new RuntimeException("Could not resolve meta file for type " + type.getSimpleName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
