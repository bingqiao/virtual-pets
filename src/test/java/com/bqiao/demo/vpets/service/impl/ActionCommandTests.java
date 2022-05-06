package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Action;
import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.domain.PetType;
import com.bqiao.demo.vpets.domain.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActionCommandTests {
    private ActionCommand underTest;
    private final String PET_ID = "pet-id";
    private final String PET_NAME = "pet-name";
    private final String PLAYER_ID = "player-id";
    private final String PET_TYPE = "catty";
    private final String PET_DESCRIPTION = "catty-desc";
    private final String ACTION_STROKE = "action-stroke";
    private final String ACTION_FEED = "action-feed";
    private final String ACTION_INVALID = "action-invalid";
    private final String PROP_HAP = "prop-hap";
    private final long PROP_HAP_MAX = 100;
    private final long PROP_HAP_MIN = 0;
    private final int PROP_HAP_RATE = -12;
    private final long PROP_HUNG_MAX = 50;
    private final long PROP_HUNG_MIN = -50;
    private final int PROP_HUNG_RATE = 5;
    private final String PROP_HUNG = "prop-hung";
    private final int ACTION_STROKE_EFFECT = 20;
    private final int ACTION_FEED_EFFECT = -10;
    private final Instant LAST_ACTION_PLAYED = Instant.parse("2018-06-25T05:12:35Z");

    @Test
    public void givenValidAction_whenApplyWithoutTimeLapse_thenEffectApplied() {
        int hap = 50;
        int hung = 0;
        Pet pet = createTestPet(hung, hap, "minute");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(ACTION_FEED)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(0, ChronoUnit.MINUTES)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung + ACTION_FEED_EFFECT), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(hap), actual.getProperties().get(PROP_HAP));
    }

    @Test
    public void givenValidAction_whenApplyWithoutTimeLapseResultingInOverflow_thenPropertyUpdatedToLimit() {
        int hap = 98;
        int hung = 50;
        int lapse = 0;
        Pet pet = createTestPet(hung, hap, "second");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(ACTION_STROKE)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.SECONDS)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(PROP_HAP_MAX), actual.getProperties().get(PROP_HAP));
    }

    @Test
    public void givenValidAction_whenApply_thenEffectAppliedAndAllPropertiesUpdated() {
        int hap = 50;
        int hung = 0;
        int lapse = 2;
        Pet pet = createTestPet(hung, hap, "minute");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(ACTION_FEED)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.MINUTES)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung + PROP_HUNG_RATE * lapse + ACTION_FEED_EFFECT), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(hap + PROP_HAP_RATE * lapse), actual.getProperties().get(PROP_HAP));
    }

    @Test
    public void givenInValidAction_whenApplyWithoutTimeLapse_thenPropertiesUnchanged() {
        int hap = 50;
        int hung = 0;
        Pet pet = createTestPet(hung, hap, "minute");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(ACTION_INVALID)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(0, ChronoUnit.MINUTES)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(hap), actual.getProperties().get(PROP_HAP));
        Assertions.assertEquals(actual.getLastActionPlayed(), Date.from(LAST_ACTION_PLAYED));
    }

    @Test
    public void givenNullAction_whenApplyWithoutTimeLapse_thenPropertiesUnchanged() {
        int hap = 50;
        int hung = 0;
        Pet pet = createTestPet(hung, hap, "minute");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(null)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(0, ChronoUnit.MINUTES)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(hap), actual.getProperties().get(PROP_HAP));
    }

    @Test
    public void givenNullAction_whenApplyWithTimeLapse_thenPropertiesUpdated() {
        int hap = 50;
        int hung = 0;
        int lapse = 3;
        Pet pet = createTestPet(hung, hap, "day");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(null)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.DAYS)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung + PROP_HUNG_RATE * lapse), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(hap + PROP_HAP_RATE * lapse), actual.getProperties().get(PROP_HAP));
    }

    @Test
    public void givenNullAction_whenApplyWithTimeLapseResultingInOverflow_thenPropertiesUpdatedToLimits() {
        int hap = 50;
        int hung = 0;
        int lapse = 100;
        Pet pet = createTestPet(hung, hap, "second");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(null)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.SECONDS)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(PROP_HUNG_MAX), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(PROP_HAP_MIN), actual.getProperties().get(PROP_HAP));
        Assertions.assertEquals(actual.getLastActionPlayed(), Date.from(LAST_ACTION_PLAYED));
    }

    @Test
    public void givenValidAction_whenApplyWithTimeLapseResultingInOverflowFromAction_thenPropertyUpdatedToLimit() {
        int hap = 99;
        int hung = -50;
        int lapse = 1;
        Pet pet = createTestPet(hung, hap, "second");
        ActionCommand underTest = ActionCommand.builder()
                .actionCode(ACTION_STROKE)
                .pet(pet)
                .actionPlayed(Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.SECONDS)))
                .build();
        Pet actual = underTest.execute();
        Assertions.assertEquals(String.valueOf(hung + PROP_HUNG_RATE * lapse), actual.getProperties().get(PROP_HUNG));
        Assertions.assertEquals(String.valueOf(PROP_HAP_MAX), actual.getProperties().get(PROP_HAP));
        Assertions.assertEquals(actual.getLastActionPlayed(), Date.from(LAST_ACTION_PLAYED.plus(lapse, ChronoUnit.SECONDS)));
    }

    private Pet createTestPet(int hung, int hap, String rateUnit) {
        Map<String, Action> actions = new HashMap<>();
        Action actionStroke = Action.builder()
                .code(ACTION_STROKE)
                .target(PROP_HAP)
                .effect(ACTION_STROKE_EFFECT)
                .build();
        Action actionFeed = Action.builder()
                .code(ACTION_FEED)
                .target(PROP_HUNG)
                .effect(ACTION_FEED_EFFECT)
                .build();
        actions.put(ACTION_STROKE, actionStroke);
        actions.put(ACTION_FEED, actionFeed);

        Map<String, Property> properties = new HashMap<>();
        Property propHap = Property.builder()
                .code(PROP_HAP)
                .max(PROP_HAP_MAX)
                .min(PROP_HAP_MIN)
                .rate(PROP_HAP_RATE)
                .rateUnit(rateUnit)
                .build();

        Property propHung = Property.builder()
                .code(PROP_HAP)
                .max(PROP_HUNG_MAX)
                .min(PROP_HUNG_MIN)
                .rate(PROP_HUNG_RATE)
                .rateUnit(rateUnit)
                .build();

        properties.put(PROP_HAP, propHap);
        properties.put(PROP_HUNG, propHung);
        PetType type = PetType.builder()
                .code(PET_TYPE)
                .description(PET_DESCRIPTION)
                .actions(actions)
                .properties(properties)
                .build();

        Map<String, String> props = new HashMap<>();
        props.put(PROP_HUNG, String.valueOf(hung));
        props.put(PROP_HAP, String.valueOf(hap));
        return Pet.builder()
                .id(PET_ID)
                .name(PET_NAME)
                .lastActionPlayed(Date.from(LAST_ACTION_PLAYED))
                .playerId(PLAYER_ID)
                .type(type)
                .properties(props)
                .build();
    }

}
