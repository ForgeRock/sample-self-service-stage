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
package org.forgerock.selfservice.sample.integration;

import java.util.ArrayList;
import java.util.List;

import org.forgerock.http.routing.Version;
import org.forgerock.openam.selfservice.KeyStoreJwtTokenConfig;
import org.forgerock.openam.selfservice.config.ServiceConfigProvider;
import org.forgerock.openam.selfservice.config.beans.ForgottenPasswordConsoleConfig;
import org.forgerock.selfservice.core.StorageType;
import org.forgerock.selfservice.core.config.ProcessInstanceConfig;
import org.forgerock.selfservice.core.config.StageConfig;
import org.forgerock.selfservice.sample.stage.MathStageConfig;
import org.forgerock.selfservice.stages.reset.ResetStageConfig;
import org.forgerock.selfservice.stages.user.UserQueryConfig;
import org.forgerock.services.context.Context;

/**
 * Config provider provides the integration between AM and self service and the intended flow.
 */
public final class CustomForgottenPasswordConfigProvider implements ServiceConfigProvider<ForgottenPasswordConsoleConfig> {

    @Override
    public boolean isServiceEnabled(ForgottenPasswordConsoleConfig config) {
        return config.isEnabled();
    }

    @Override
    public ProcessInstanceConfig getServiceConfig(ForgottenPasswordConsoleConfig config, Context context, String realm) {
        List<StageConfig> stages = new ArrayList<>();

        stages.add(new UserQueryConfig()
                .setValidQueryFields(config.getValidQueryAttributes())
                .setIdentityIdField("/username")
                .setIdentityUsernameField("/username")
                .setIdentityEmailField("/" + config.getEmailAttributeName() + "/0")
                .setIdentityServiceUrl("/users")
                .setResourceVersion(Version.version(3, 0)));

        int leftHandValue = Integer.parseInt(config.getAttributeAsString("selfServiceMathLeftSide"));
        int rightHandValue = Integer.parseInt(config.getAttributeAsString("selfServiceMathRightSide"));

        stages.add(new MathStageConfig()
                .withLeftHandValue(leftHandValue)
                .withRightHandValue(rightHandValue));

        stages.add(new ResetStageConfig()
                .setIdentityServiceUrl("/users")
                .setIdentityPasswordField("userPassword"));

        KeyStoreJwtTokenConfig extendedJwtTokenConfig = new KeyStoreJwtTokenConfig()
                .withEncryptionKeyPairAlias(config.getEncryptionKeyPairAlias())
                .withSigningSecretKeyAlias(config.getSigningSecretKeyAlias())
                .withTokenLifeTimeInSeconds(config.getTokenExpiry());

        return new ProcessInstanceConfig()
                .setStageConfigs(stages)
                .setSnapshotTokenConfig(extendedJwtTokenConfig)
                .setStorageType(StorageType.STATELESS);
    }

}
