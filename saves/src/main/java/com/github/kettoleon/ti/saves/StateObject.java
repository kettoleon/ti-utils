package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.kettoleon.ti.TiObjectMapper;
import com.github.kettoleon.ti.meta.model.MetaObject;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

public abstract class StateObject {

    private final SaveFile saveFile;
    private final JsonNode node;

    public StateObject(SaveFile saveFile, JsonNode node) {
        this.saveFile = saveFile;
        this.node = node;
    }

    protected <T extends MetaObject> T getMetadata(Class<T> cls) {
        return saveFile.getMetaReference(getTemplateName(), cls).orElseThrow();
    }

    protected <T extends StateObject> Optional<T> getReference(Class<T> cls, String... field) {
        Optional<Integer> referenceId = getReferenceId(field);
        if (referenceId.isEmpty()) {
            return Optional.empty();
        }
        return saveFile.getReference(referenceId.get(), cls);
    }


    protected <T extends MetaObject> Optional<T> getMetaReference(String field, Class<T> cls) {
        return saveFile.getMetaReference(node.path(field).asText(), cls);
    }

    protected <T extends StateObject> List<T> getReferenceArray(String field, Class<T> cls) {
        List<T> refs = new ArrayList<>();
        node.path(field).forEach(ref -> saveFile.getReference(ref.path("value").asInt(), cls).ifPresent(refs::add));
        return refs;
    }

    protected <T extends MetaObject> List<T> getMetaReferenceArray(String field, Class<T> cls) {
        List<T> refs = new ArrayList<>();
        node.path(field).forEach(ref -> saveFile.getMetaReference(ref.asText(), cls).ifPresent(refs::add));
        return refs;
    }

    @Deprecated
    protected SaveFile getSaveFile() {
        return saveFile;
    }

    @Deprecated
    protected JsonNode getNode() {
        return node;
    }

    protected Optional<Integer> getReferenceId(String... field) {
        JsonNode ref = navigateTo(field);
        if (ref.hasNonNull("value")) {
            ref = ref.path("value");
        }
        if (ref.isNull() || ref.isMissingNode()) {
            return Optional.empty();
        }
        return Optional.of(ref.asInt());
    }

    public int getId() {
        return getReferenceId("ID").orElseThrow();
    }

    public String getClassType() {
        return getString("$type");
    }

    protected int getInt(String... field) {
        return navigateTo(field).asInt();
    }

    protected double getDouble(String... field) {
        return navigateTo(field).asDouble();
    }

    private JsonNode navigateTo(String[] field) {
        JsonNode nav = node;
        for (String f : field) {
            nav = nav.path(f);
        }
        return nav;
    }

    protected String getString(String... field) {
        return navigateTo(field).asText();
    }

    protected List<String> getStringList(String... field) {
        List<String> list = new ArrayList<>();
        navigateTo(field).forEach(vn -> list.add(vn.asText()));
        return list;
    }

    protected <T extends StateObject> List<T> getArray(Class<T> cls, String... field) {
        List<T> list = new ArrayList<>();

        navigateTo(field).forEach(vn -> {
            try {
                list.add(cls.getConstructor(SaveFile.class, JsonNode.class).newInstance(saveFile, vn));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        return list;
    }

    protected <K extends StateObject, V> Map<K, V> getReferenceMap(Class<K> keyClass, Class<V> valueClass, String... field) {
        Map<K, V> map = new LinkedHashMap<>();

        navigateTo(field).forEach(vn -> {
            map.put(saveFile.getReference(vn.path("Key").path("value").asInt(), keyClass).orElseThrow(), readAs(vn.path("Value"), valueClass));
        });


        return map;
    }

    protected <K extends StateObject> List<K> getReferenceMapKeysOnly(Class<K> keyClass, String... field) {
        List<K> list = new ArrayList<>();

        navigateTo(field).forEach(vn -> {
            list.add(saveFile.getReference(vn.path("Key").path("value").asInt(), keyClass).orElseThrow());
        });


        return list;
    }

    private <V> V readAs(JsonNode value, Class<V> valueClass) {
        try {
            return TiObjectMapper.TI_OBJECT_MAPPER.treeToValue(value, valueClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean getBoolean(String... field) {
        return navigateTo(field).asBoolean();
    }

    protected LocalDateTime getLocalDateTime(String... field) {
        JsonNode dtn = navigateTo(field);

        return LocalDateTime.of(
                dtn.path("year").asInt(),
                dtn.path("month").asInt(),
                dtn.path("day").asInt(),
                dtn.path("hour").asInt(),
                dtn.path("minute").asInt(),
                dtn.path("second").asInt()
        );
    }

    public String getTemplateName() {
        return getString("templateName");
    }

    public String getDisplayName() {
        return getString("displayName");
    }

    public boolean isArchived() {
        return getBoolean("archived");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StateObject) {
            StateObject other = (StateObject) obj;
            return getId() == other.getId();
        }
        return false;
    }
}
