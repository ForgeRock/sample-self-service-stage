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

import java.util.Objects;

import org.forgerock.selfservice.core.config.StageConfig;

/**
 * Configuration required by the sample math stage.
 */
public final class MathStageConfig implements StageConfig {

    private static final String STAGE_NAME = "mathStage";

    private int leftHandValue;
    private int rightHandValue;

    public int getLeftHandValue() {
        return leftHandValue;
    }

    public MathStageConfig withLeftHandValue(int leftHandValue) {
        this.leftHandValue = leftHandValue;
        return this;
    }

    public int getRightHandValue() {
        return rightHandValue;
    }

    public MathStageConfig withRightHandValue(int rightHandValue) {
        this.rightHandValue = rightHandValue;
        return this;
    }

    public String getProgressStageClassName() {
        return MathStage.class.getName();
    }

    public String getName() {
        return STAGE_NAME;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof MathStageConfig)) {
            return false;
        }

        MathStageConfig config = (MathStageConfig) other;
        return leftHandValue == config.leftHandValue &&
                rightHandValue == config.rightHandValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftHandValue, rightHandValue);
    }

}
