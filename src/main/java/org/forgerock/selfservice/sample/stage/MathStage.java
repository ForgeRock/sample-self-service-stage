/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 ForgeRock AS.
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
