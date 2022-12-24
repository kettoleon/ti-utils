package com.github.kettoleon.ti.saves;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.kettoleon.ti.meta.MetaRepository;
import com.github.kettoleon.ti.meta.model.MetaObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.function.Predicate.not;

public class SaveFile {

    public static final JsonMapper objectMapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .build();
    private final JsonNode root;
    private HashMap<Integer, Player> players;
    private HashMap<Integer, Mission> missions;
    private MetaRepository meta;

    public SaveFile(Path file, MetaRepository meta) {
        this.meta = meta;
        try {
            InputStream fileStream = new FileInputStream(file.toFile());
            InputStream gzipStream = new GZIPInputStream(fileStream);
            Reader decoder = new InputStreamReader(gzipStream);
            BufferedReader buffered = new BufferedReader(decoder);

            root = objectMapper.readTree(buffered);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return root.path("currentID").path("value").asInt();
    }

    public Map<Integer, Faction> getFactions() {
        return getAllStateObjects(Faction.class);
    }

    public Map<Integer, Player> getPlayers() {
        if (players == null) {
            players = new HashMap<>();
            root.path("gamestates").path("PavonisInteractive.TerraInvicta.TIPlayerState").forEach(
                    factionNode -> {
                        int factionId = factionNode.path("Key").path("value").asInt();
                        Player faction = new Player(this, factionId, factionNode.path("Value"));
                        players.put(factionId, faction);
                    }
            );
        }
        return players;
    }


    public Optional<Faction> getFactionById(int factionId) {
        return Optional.ofNullable(getFactions().get(factionId));
    }

    public Faction getPlayerFaction() {
        return getPlayers().values().stream()
                .filter(not(Player::isAI))
                .map(Player::getFaction)
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow();
    }

    public Faction getAlienFaction() {
        return getFactions().values().stream()
                .filter(Faction::isAlien)
                .findFirst()
                .orElseThrow();
    }

    public Player getPlayerById(int playerId) {
        return getPlayers().get(playerId);
    }

    public Optional<Councilor> getCouncilorById(int councilorId) {
        return Optional.ofNullable(getCouncilors().get(councilorId));
    }

    public Map<Integer, Councilor> getCouncilors() {
        return getAllStateObjects(Councilor.class);
    }

    private Map<Integer, Mission> getMissions() {
        if (missions == null) {
            missions = new HashMap<>();
            root.path("gamestates").path("PavonisInteractive.TerraInvicta.TIMissionState").forEach(
                    councilorNode -> {
                        int councilorId = councilorNode.path("Key").path("value").asInt();
                        Mission councilor = new Mission(this, councilorId, councilorNode.path("Value"));
                        missions.put(councilorId, councilor);
                    }
            );
        }
        return missions;
    }

    private static final Map<Class<? extends StateObject>, String> javaClassToSaveClass = initRegistry();
    private final Map<Class<? extends StateObject>, Map<Integer, ? extends StateObject>> cache = new HashMap<>();


    private static Map<Class<? extends StateObject>, String> initRegistry() {
        HashMap<Class<? extends StateObject>, String> registry = new HashMap<>();
        registry.put(TimeState.class, "PavonisInteractive.TerraInvicta.TITimeState");
        registry.put(Councilor.class, "PavonisInteractive.TerraInvicta.TICouncilorState");
        registry.put(Faction.class, "PavonisInteractive.TerraInvicta.TIFactionState");
        registry.put(Region.class, "PavonisInteractive.TerraInvicta.TIRegionState");
        registry.put(Nation.class, "PavonisInteractive.TerraInvicta.TINationState");
        registry.put(Federation.class, "PavonisInteractive.TerraInvicta.TIFederationState");
        registry.put(Organisation.class, "PavonisInteractive.TerraInvicta.TIOrgState");
        registry.put(GlobalResearch.class, "PavonisInteractive.TerraInvicta.TIGlobalResearchState");
        return registry;
    }

    public List<Nation> getNationsThatCurrentlyExist() {
        return getAllStateObjects(Region.class).values().stream()
                .flatMap(r -> r.getNation().stream()).distinct().collect(Collectors.toList());
    }

    public <T extends StateObject> Map<Integer, T> getAllStateObjects(Class<T> cls) {
        if (!cache.containsKey(cls)) {
            Map<Integer, T> objects = new HashMap<>();

            root.path("gamestates").path(javaClassToSaveClass.get(cls)).forEach(node -> {
                try {
                    T obj = cls.getConstructor(SaveFile.class, JsonNode.class).newInstance(this, node.path("Value"));
                    objects.put(obj.getId(), obj);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            cache.put(cls, objects);
        }
        return (Map<Integer, T>) cache.get(cls);
    }

    public <T extends StateObject> T getSingletonStateObject(Class<T> cls) {
        Map<Integer, T> allStateObjects = getAllStateObjects(cls);
        if (allStateObjects.size() != 1) {
            throw new RuntimeException("Expected a single " + cls.getName() + " in the save file, but found: " + allStateObjects.size());
        }
        return allStateObjects.values().stream().findFirst().orElseThrow();
    }

    public <T extends StateObject> Optional<T> getReference(int referenceId, Class<T> cls) {
        return Optional.ofNullable(getAllStateObjects(cls).get(referenceId));
    }

    public LocalDate getCurrentDate() {
        return getSingletonStateObject(TimeState.class).getCurrentDateTime().toLocalDate();
    }

    public <T extends MetaObject> Optional<T> getMetaReference(String templateName, Class<T> cls) {
        return meta.get(templateName, cls);
    }

    public List<Federation> getFormedFederations() {
        return getAllStateObjects(Federation.class).values().stream()
                .filter(fed -> !fed.getMembers().isEmpty()).collect(Collectors.toList());
    }
}
