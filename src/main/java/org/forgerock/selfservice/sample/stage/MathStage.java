/*
 * Copyright 2018 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package org.forgerock.selfservice.sample.stage;

import static java.lang.String.format;

import org.forgerock.json.JsonValue;
import org.forgerock.json.resource.BadRequestException;
import org.forgerock.json.resource.ResourceException;
import org.forgerock.selfservice.core.ProcessContext;
import org.forgerock.selfservice.core.ProgressStage;
import org.forgerock.selfservice.core.StageResponse;
import org.forgerock.selfservice.core.util.RequirementsBuilder;

/**
 * Sample math stage to present the user with a basic math challenge.
 */
public final class MathStage implements ProgressStage<MathStageConfig> {

    private static final String PROBLEM = "What is %d + %d?";
    private static final String ANSWER = "math-answer";

    public JsonValue gatherInitialRequirements(ProcessContext context,
            MathStageConfig config) throws ResourceException {
        return RequirementsBuilder
                .newInstance("Math Challenge")
                .addRequireProperty(ANSWER,
                        format(PROBLEM, config.getLeftHandValue(), config.getRightHandValue()))
                .build();
    }

    public StageResponse advance(ProcessContext context, MathStageConfig config) throws ResourceException {
        if (!context.getInput().isDefined(ANSWER)) {
            throw new BadRequestException("Math answer is missing");
        }

        int givenAnswer = parseAnswer(context.getInput());

        if (givenAnswer != config.getLeftHandValue() + config.getRightHandValue()) {
            throw new BadRequestException("Math answer is invalid");
        }

        return StageResponse
                .newBuilder()
                .build();
    }

    private int parseAnswer(JsonValue input) throws BadRequestException {
        try {
            return Integer.parseInt(input.get(ANSWER).asString());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Math answer must be a number", e);
        }
    }

}
