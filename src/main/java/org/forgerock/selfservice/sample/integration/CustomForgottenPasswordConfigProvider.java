/*
 * Copyright 2018 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
