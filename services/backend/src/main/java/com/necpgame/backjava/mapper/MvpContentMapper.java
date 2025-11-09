package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.mvp.MvpContentOverviewEntity;
import com.necpgame.backjava.entity.mvp.MvpContentOverviewEventEntity;
import com.necpgame.backjava.entity.mvp.MvpContentStatusEntity;
import com.necpgame.backjava.entity.mvp.MvpEndpointEntity;
import com.necpgame.backjava.entity.mvp.MvpModelEntity;
import com.necpgame.backjava.entity.mvp.MvpModelFieldEntity;
import com.necpgame.backjava.entity.mvp.MvpTextActionEntity;
import com.necpgame.backjava.entity.mvp.MvpTextNearbyNpcEntity;
import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentOverviewQuestsByType;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.ContentStatusSystemsReady;
import com.necpgame.backjava.model.EndpointDefinition;
import com.necpgame.backjava.model.ModelDefinition;
import com.necpgame.backjava.model.ModelDefinitionFieldsInner;
import com.necpgame.backjava.model.TextVersionStateAvailableActionsInner;
import com.necpgame.backjava.model.TextVersionStateNearbyNpcsInner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MvpContentMapper {

    @Mapping(target = "method", expression = "java(mapMethod(entity.getMethod()))")
    @Mapping(target = "priority", expression = "java(mapPriority(entity.getPriority()))")
    EndpointDefinition toEndpointDefinition(MvpEndpointEntity entity);

    @Mapping(target = "fields", expression = "java(mapFields(entity.getFields()))")
    ModelDefinition toModelDefinition(MvpModelEntity entity);

    @Mapping(target = "fieldName", source = "fieldName")
    @Mapping(target = "type", source = "fieldType")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "description", source = "description")
    ModelDefinitionFieldsInner toModelDefinitionField(MvpModelFieldEntity entity);

    @Mapping(target = "questsByType", expression = "java(toQuestsByType(entity))")
    @Mapping(target = "keyEvents", expression = "java(mapKeyEvents(entity.getKeyEvents()))")
    @Mapping(target = "implementedPercentage", expression = "java(toFloat(entity.getImplementedPercentage()))")
    ContentOverview toContentOverview(MvpContentOverviewEntity entity);

    @Mapping(target = "systemsReady", expression = "java(toSystemsReady(entity))")
    ContentStatus toContentStatus(MvpContentStatusEntity entity);

    @Mapping(target = "action", source = "action")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "command", source = "command")
    TextVersionStateAvailableActionsInner toTextVersionAction(MvpTextActionEntity entity);

    @Mapping(target = "name", source = "npcName")
    @Mapping(target = "canInteract", source = "canInteract")
    TextVersionStateNearbyNpcsInner toTextVersionNearbyNpc(MvpTextNearbyNpcEntity entity);

    default EndpointDefinition.MethodEnum mapMethod(String method) {
        return method == null ? null : EndpointDefinition.MethodEnum.fromValue(method);
    }

    default EndpointDefinition.PriorityEnum mapPriority(MvpEndpointEntity.Priority priority) {
        return priority == null ? null : EndpointDefinition.PriorityEnum.fromValue(priority.name());
    }

    default List<ModelDefinitionFieldsInner> mapFields(List<MvpModelFieldEntity> fields) {
        return fields == null
            ? List.of()
            : fields.stream()
                .map(this::toModelDefinitionField)
                .collect(Collectors.toList());
    }

    default ContentOverviewQuestsByType toQuestsByType(MvpContentOverviewEntity entity) {
        ContentOverviewQuestsByType questsByType = new ContentOverviewQuestsByType();
        questsByType.setMain(entity.getMainQuests());
        questsByType.setSide(entity.getSideQuests());
        questsByType.setFaction(entity.getFactionQuests());
        return questsByType;
    }

    default List<String> mapKeyEvents(List<MvpContentOverviewEventEntity> events) {
        return events == null
            ? List.of()
            : events.stream()
                .map(MvpContentOverviewEventEntity::getEventDescription)
                .collect(Collectors.toList());
    }

    default Float toFloat(BigDecimal value) {
        return value == null ? null : value.floatValue();
    }

    default ContentStatusSystemsReady toSystemsReady(MvpContentStatusEntity entity) {
        ContentStatusSystemsReady systemsReady = new ContentStatusSystemsReady();
        systemsReady.setQuestEngine(entity.isQuestEngineReady());
        systemsReady.setCombat(entity.isCombatReady());
        systemsReady.setProgression(entity.isProgressionReady());
        systemsReady.setSocial(entity.isSocialReady());
        systemsReady.setEconomy(entity.isEconomyReady());
        return systemsReady;
    }

    default Map<String, Long> toCategoryMap(List<MvpEndpointEntity> endpoints) {
        return endpoints.stream()
            .collect(Collectors.groupingBy(MvpEndpointEntity::getCategory, Collectors.counting()));
    }
}

