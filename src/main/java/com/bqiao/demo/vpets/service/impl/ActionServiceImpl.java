package com.bqiao.demo.vpets.service.impl;

import com.bqiao.demo.vpets.domain.Pet;
import com.bqiao.demo.vpets.repo.PetRepo;
import com.bqiao.demo.vpets.service.ActionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ActionServiceImpl implements ActionService {
    private final PetRepo repo;

    @Value("${metadata:1}")
    private String metadataId;

    public ActionServiceImpl(PetRepo repo) {
        this.repo = repo;
    }
    @Override
    public Pet apply(Pet pet, String action) {
        ActionCommand ac = ActionCommand.builder()
                    .actionCode(action)
                    .pet(pet)
                    .actionPlayed(new Date())
                    .build();
        return ac.execute();
    }
}
