package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.narrative.NarrativeNpcNameEntity;
import com.necpgame.backjava.entity.narrative.NarrativeNpcTemplateEntity;
import com.necpgame.backjava.entity.narrative.NarrativeQuestBlueprintEntity;
import com.necpgame.backjava.entity.narrative.NarrativeToolsWeightedEntity;
import com.necpgame.backjava.model.GenerateNPC200Response;
import com.necpgame.backjava.model.GenerateNPCRequest;
import com.necpgame.backjava.model.GenerateQuestRequest;
import com.necpgame.backjava.model.ValidateNarrative200Response;
import com.necpgame.backjava.model.ValidateNarrativeRequest;
import com.necpgame.backjava.repository.narrative.NarrativeNpcNameRepository;
import com.necpgame.backjava.repository.narrative.NarrativeNpcTemplateRepository;
import com.necpgame.backjava.repository.narrative.NarrativeQuestBlueprintRepository;
import com.necpgame.backjava.service.NarrativeToolsService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NarrativeToolsServiceImpl implements NarrativeToolsService {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<List<Map<String, Object>>> LIST_OF_MAPS_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<List<Object>> LIST_OF_OBJECTS_TYPE = new TypeReference<>() {
    };

    private final NarrativeNpcTemplateRepository npcTemplateRepository;
    private final NarrativeNpcNameRepository npcNameRepository;
    private final NarrativeQuestBlueprintRepository questBlueprintRepository;
    private final ObjectMapper objectMapper;

    @Override
    public GenerateNPC200Response generateNPC(GenerateNPCRequest generateNPCRequest) {
        NarrativeNpcTemplateEntity template = resolveTemplate(generateNPCRequest);

        String name = resolveName(template, generateNPCRequest)
            .orElseGet(() -> buildFallbackName(template));

        Map<String, Object> personality = readJsonMap(template.getPersonalityJson(), "personality");
        String backstory = renderBackstory(template.getBackstoryTemplate(), name, template.getTemplateCode());

        log.debug(
            "Generated NPC template={} faction={} region={} role={}",
            template.getTemplateCode(),
            template.getFaction(),
            template.getRegion(),
            template.getRole()
        );

        return new GenerateNPC200Response()
            .npcId(UUID.randomUUID().toString())
            .name(name)
            .personality(personality)
            .backstory(backstory);
    }

    @Override
    public Object generateQuest(GenerateQuestRequest generateQuestRequest) {
        NarrativeQuestBlueprintEntity blueprint = resolveBlueprint(generateQuestRequest);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("quest_id", blueprint.getBlueprintCode() + "-" + randomSuffix());
        response.put("title", blueprint.getTitle());
        response.put("type", blueprint.getQuestType());
        response.put("difficulty", blueprint.getDifficulty());
        response.put("region", blueprint.getRegion());
        response.put("summary", blueprint.getSummary());
        response.put("recommended_level", blueprint.getRecommendedLevel());
        response.put("expiry", blueprint.getExpiryDays());
        response.put("objectives", readJsonList(blueprint.getObjectivesJson(), "objectives"));
        response.put("rewards", readJsonMap(blueprint.getRewardsJson(), "rewards"));
        response.put("hooks", readJsonArray(blueprint.getHooksJson(), "hooks"));

        log.debug(
            "Generated quest blueprint={} type={} difficulty={} region={}",
            blueprint.getBlueprintCode(),
            blueprint.getQuestType(),
            blueprint.getDifficulty(),
            blueprint.getRegion()
        );

        return response;
    }

    @Override
    public ValidateNarrative200Response validateNarrative(ValidateNarrativeRequest validateNarrativeRequest) {
        List<Map<String, Object>> questSequence = Optional.ofNullable(validateNarrativeRequest.getQuestSequence())
            .orElseGet(ArrayList::new);
        List<Map<String, Object>> playerChoices = Optional.ofNullable(validateNarrativeRequest.getPlayerChoices())
            .orElseGet(ArrayList::new);

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (questSequence.isEmpty()) {
            errors.add("Пустая последовательность квестов");
            return buildValidationResponse(errors, warnings);
        }

        validateSequenceOrder(questSequence, errors, warnings);
        validateDependencies(questSequence, errors);
        validateBranchingConsistency(questSequence, playerChoices, errors, warnings);
        validateChoiceCoverage(questSequence, playerChoices, warnings);

        return buildValidationResponse(errors, warnings);
    }

    private NarrativeNpcTemplateEntity resolveTemplate(GenerateNPCRequest request) {
        List<NarrativeNpcTemplateEntity> candidates = npcTemplateRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addEqualsPredicate(predicates, builder, root.get("templateCode"), request.getTemplate());
            addEqualsPredicate(predicates, builder, root.get("faction"), request.getFaction());
            addEqualsPredicate(predicates, builder, root.get("region"), request.getRegion());
            addEqualsPredicate(predicates, builder, root.get("role"), request.getRole());

            if (predicates.isEmpty()) {
                return query.getRestriction();
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });

        if (candidates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найден подходящий шаблон NPC");
        }

        return pickWeighted(candidates);
    }

    private Optional<String> resolveName(NarrativeNpcTemplateEntity template, GenerateNPCRequest request) {
        String region = Optional.ofNullable(request.getRegion()).orElse(template.getRegion());
        String role = Optional.ofNullable(request.getRole()).orElse(template.getRole());

        return npcNameRepository.findPreferredNames(region, role)
            .stream()
            .sorted(Comparator.comparingInt(NarrativeNpcNameEntity::getWeight).reversed())
            .findFirst()
            .map(NarrativeNpcNameEntity::getName);
    }

    private NarrativeQuestBlueprintEntity resolveBlueprint(GenerateQuestRequest request) {
        List<NarrativeQuestBlueprintEntity> blueprints = questBlueprintRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addEqualsPredicate(predicates, builder, root.get("questType"), request.getType());
            addEqualsPredicate(predicates, builder, root.get("difficulty"), request.getDifficulty());
            addEqualsPredicate(predicates, builder, root.get("region"), request.getRegion());

            if (predicates.isEmpty()) {
                return query.getRestriction();
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });

        if (blueprints.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не найден подходящий шаблон квеста");
        }

        return pickWeighted(blueprints);
    }

    private void addEqualsPredicate(List<Predicate> predicates, CriteriaBuilder builder, Path<String> path, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(builder.equal(builder.lower(path), value.toLowerCase(Locale.ROOT)));
        }
    }

    private <T extends NarrativeToolsWeightedEntity> T pickWeighted(List<T> records) {
        int totalWeight = records.stream()
            .mapToInt(record -> Math.max(record.getWeight(), 1))
            .sum();

        int threshold = ThreadLocalRandom.current().nextInt(totalWeight);
        int accumulated = 0;

        for (T record : records) {
            accumulated += Math.max(record.getWeight(), 1);
            if (threshold < accumulated) {
                return record;
            }
        }
        return records.get(records.size() - 1);
    }

    private Map<String, Object> readJsonMap(String json, String fieldName) {
        if (!StringUtils.hasText(json)) {
            return new LinkedHashMap<>();
        }
        try {
            return objectMapper.readValue(json, MAP_TYPE);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка обработки шаблона " + fieldName, ex);
        }
    }

    private List<Map<String, Object>> readJsonList(String json, String fieldName) {
        if (!StringUtils.hasText(json)) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, LIST_OF_MAPS_TYPE);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка обработки шаблона " + fieldName, ex);
        }
    }

    private List<Object> readJsonArray(String json, String fieldName) {
        if (!StringUtils.hasText(json)) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, LIST_OF_OBJECTS_TYPE);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка обработки шаблона " + fieldName, ex);
        }
    }

    private String renderBackstory(String template, String name, String templateCode) {
        if (!StringUtils.hasText(template)) {
            return String.format(Locale.ROOT, "%s родился в неизвестных кварталах города и скрывает своё прошлое.", name);
        }
        return template
            .replace("{name}", name)
            .replace("{template}", templateCode)
            .replace("{year}", String.valueOf(OffsetDateTime.now().getYear()));
    }

    private String buildFallbackName(NarrativeNpcTemplateEntity template) {
        String region = Optional.ofNullable(template.getRegion()).orElse("nomad");
        String role = Optional.ofNullable(template.getRole()).orElse("specialist");
        return (
            region.substring(0, Math.min(region.length(), 3)) +
            "-" +
            role.substring(0, Math.min(role.length(), 3)) +
            "-" +
            randomSuffix()
        ).toUpperCase(Locale.ROOT);
    }

    private void validateSequenceOrder(List<Map<String, Object>> questSequence, List<String> errors, List<String> warnings) {
        Set<String> questIds = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (int index = 0; index < questSequence.size(); index++) {
            Map<String, Object> entry = questSequence.get(index);
            String questId = readString(entry, "quest_id");
            if (!StringUtils.hasText(questId)) {
                errors.add(String.format(Locale.ROOT, "Элемент #%d не содержит quest_id", index + 1));
                continue;
            }
            if (!questIds.add(questId)) {
                duplicates.add(questId);
            }
            Object stageValue = entry.get("stage");
            if (stageValue != null && !(stageValue instanceof Number)) {
                warnings.add(String.format(Locale.ROOT, "Квест %s имеет некорректное значение stage", questId));
            }
        }

        duplicates.stream()
            .sorted()
            .forEach(duplicate -> warnings.add("Квест " + duplicate + " встречается несколько раз"));
    }

    private void validateDependencies(List<Map<String, Object>> questSequence, List<String> errors) {
        Map<String, List<String>> dependencies = new HashMap<>();
        for (Map<String, Object> quest : questSequence) {
            String questId = readString(quest, "quest_id");
            dependencies.put(questId, readStringList(quest.get("depends_on")));
        }

        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();

        for (String questId : dependencies.keySet()) {
            if (detectCycle(questId, dependencies, visited, stack)) {
                errors.add("Обнаружена циклическая зависимость для квеста " + questId);
            }
        }
    }

    private boolean detectCycle(String questId, Map<String, List<String>> dependencies, Set<String> visited, Set<String> stack) {
        if (!visited.add(questId)) {
            return false;
        }
        stack.add(questId);
        for (String dependency : dependencies.getOrDefault(questId, List.of())) {
            if (!dependencies.containsKey(dependency)) {
                continue;
            }
            if (!visited.contains(dependency) && detectCycle(dependency, dependencies, visited, stack)) {
                return true;
            }
            if (stack.contains(dependency)) {
                return true;
            }
        }
        stack.remove(questId);
        return false;
    }

    private void validateBranchingConsistency(
        List<Map<String, Object>> questSequence,
        List<Map<String, Object>> playerChoices,
        List<String> errors,
        List<String> warnings
    ) {
        Map<String, Set<String>> branches = new HashMap<>();
        for (Map<String, Object> quest : questSequence) {
            String questId = readString(quest, "quest_id");
            branches.put(questId, new HashSet<>(readStringList(quest.get("branches"))));
        }

        for (Map<String, Object> choice : playerChoices) {
            String questId = readString(choice, "quest_id");
            String branch = readString(choice, "branch");
            if (!StringUtils.hasText(questId) || !StringUtils.hasText(branch)) {
                continue;
            }
            if (!branches.containsKey(questId)) {
                warnings.add("Выбор ссылается на квест " + questId + ", которого нет в последовательности");
                continue;
            }
            if (!branches.get(questId).contains(branch)) {
                errors.add("Выбор ведёт в ветку " + branch + " квеста " + questId + ", которая не определена в последовательности");
            }
        }
    }

    private void validateChoiceCoverage(
        List<Map<String, Object>> questSequence,
        List<Map<String, Object>> playerChoices,
        List<String> warnings
    ) {
        Map<String, Long> choicesPerQuest = new HashMap<>();
        for (Map<String, Object> choice : playerChoices) {
            String questId = readString(choice, "quest_id");
            if (StringUtils.hasText(questId)) {
                long current = choicesPerQuest.getOrDefault(questId, 0L);
                choicesPerQuest.put(questId, current + 1L);
            }
        }

        for (Map<String, Object> quest : questSequence) {
            String questId = readString(quest, "quest_id");
            long declaredBranches = readStringList(quest.get("branches")).size();
            long recordedChoices = choicesPerQuest.getOrDefault(questId, 0L);
            if (declaredBranches > 0 && recordedChoices == 0) {
                warnings.add("Для квеста " + questId + " не зафиксировано ни одного выбора ветки");
            }
        }
    }

    private ValidateNarrative200Response buildValidationResponse(List<String> errors, List<String> warnings) {
        return new ValidateNarrative200Response()
            .valid(errors.isEmpty())
            .errors(errors)
            .warnings(warnings);
    }

    private String readString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof String str ? str : null;
    }

    private List<String> readStringList(Object raw) {
        if (raw instanceof List<?> list) {
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof String str && StringUtils.hasText(str)) {
                    result.add(str);
                }
            }
            return result;
        }
        return List.of();
    }

    private String randomSuffix() {
        return Integer.toString(ThreadLocalRandom.current().nextInt(1_000, 10_000));
    }
}
