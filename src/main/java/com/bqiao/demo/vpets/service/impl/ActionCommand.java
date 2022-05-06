package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Action;
import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.domain.Property;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Apply action against pet.
 * 1. update all rate-based properties
 * 2. apply action if valid
 * 3. update lastActionPlayed
 */
@Data
@Builder
public class ActionCommand {
    private Pet pet;
    private String actionCode;
    private Date actionPlayed;

    private void apply(Map<String, String> props, Map<String, Property> properties, Action action) {
        String target = action.getTarget();
        int effect = action.getEffect();
        String val = props.get(target);
        Property property = properties.get(target);
        if (!StringUtils.hasText(val) || property == null) {
            return;
        }
        Long value = Long.valueOf(val);
        Long updated = value;
        try {
            updated = Math.addExact(value, effect);
        } catch (ArithmeticException e) {
            if (effect > 0) {
                updated = property.getMax();
            } else {
                updated = property.getMin();
            }
        }
        if (updated > property.getMax()) {
            updated = property.getMax();
        }
        if (updated < property.getMin()) {
            updated = property.getMin();
        }
        props.put(target, String.valueOf(updated));
    }

    private Map<String, String> updatePropertiesWithRate(Map<String, Property> properties) {
        if (properties == null) {
            return null;
        }
        Map<String, String> updated = new HashMap<>();
        Date lastActionPlayed = pet.getLastActionPlayed();
        for (String key : pet.getProperties().keySet()) {
            String value = updatePropertyWithRate(pet.getProperties().get(key), properties.get(key), lastActionPlayed);
            updated.put(key, value);
        }
        return updated;
    }

    private String updatePropertyWithRate(String val, Property property, Date lastActionPlayed) {
        long value = Long.valueOf(val);
        if (property.getRate() == 0) {
            return val;
        }
        Duration dur = Duration.between(lastActionPlayed.toInstant(), actionPlayed.toInstant());
        if (dur.isNegative() || dur.isZero()) {
            return val;
        }
        long multiplier = 0;
        switch (property.getRateUnit()) {
            case "second":
                multiplier = dur.toSeconds();
                break;
            case "minute":
                multiplier = dur.toMinutes();
                break;
            case "hour":
                multiplier = dur.toHours();
                break;
            case "day":
                multiplier = dur.toDays();
                break;
            default:
                return val;
        }
        long updated = value;
        if (multiplier != 0) {
            try {
                updated = Math.addExact(Math.multiplyExact(multiplier, property.getRate()), value);
            } catch (ArithmeticException e) {
                if (property.getRate() > 0) {
                    updated = property.getMax();
                } else {
                    updated = property.getMin();
                }
            }
            if (updated > property.getMax()) {
                updated = property.getMax();
            }
            if (updated < property.getMin()) {
                updated = property.getMin();
            }
        }
        return String.valueOf(updated);
    }

    public Pet execute() {
        Action action = pet.getType().getActions().get(actionCode);
        Map<String, Property> properties = pet.getType().getProperties();
        Map<String, String> updated = updatePropertiesWithRate(properties);
        if (action != null) {
            apply(updated, properties, action);
            pet.setLastActionPlayed(actionPlayed);
        }
        pet.setProperties(updated);
        return pet;
    }
}
