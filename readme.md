<!--
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
-->
Read me for the sample self service math stage
==============================================

This project demonstrates how to modify the self service functionality of [ForgeRock](https://www.forgerock.com/)'s Identity Platoform. 'Self Service' allows users to self register and perform password reset without any need to email admins of a service and is an essential part of any identity management solution.

This sample project creates a new stage to be used within the forgotten password flow.

To create a new stage all that is needed is a stage implementation along with its configuration class.
See MathStage and MathStageConfig as a basic example. 

For a custom stage to be integrated into AM, a configuration provider is required. See 
CustomForgottenPasswordConfigProvider as an example. A configuration provider defines the set of
stages to be ran within a given self service flow. 

After the custom project is built, the jar will need to be dropped into the app server under the WEB-INF
folder of the exploded AM war. The custom configuration provider will need to be declared in the self 
service config. Navigate to realm -> services -> user self service -> advanced configuration, and there 
declare the custom configuration provider against the appropriate flow. 

## Adding configuration in AM self service UI

If UI configuration is required, the additional configuration to be added need to be declared as a 
service configuration within an XML file. See mathStageConfig.xml as an example.

Using ssoadm run the add-attrs command, like so:
`bin/ssoadm add-attrs -s selfService -t Organization -u amadmin -f password.txt -F mathStageConfig.xml`

The localisation and section properties files in AM will also need to be updated, so that the UI can 
display the appropriate text and display the configuration on the appropriate tab. To do this the following
properties files will need to be located under the exploded AM war:
`./WEB-INF/classes/selfService.properties`  
`./WEB-INF/classes/selfService.section.properties`

The selfService.properties file is used to define the localisation strings, like so:  
`math.left.side=Left hand side`  
`math.right.side=Right hand side`

The selfService.section.properties file is used to define where the configuration appears, like so:
`forgottenPassword=selfServiceMathLeftSide`  
`forgottenPassword=selfServiceMathRightSide` 

## Reference configuration
For further reference see the self service module in AM and the self service commons project:
`https://stash.forgerock.org/projects/OPENAM/repos/openam/browse/openam-selfservice`
`https://stash.forgerock.org/projects/COMMONS/repos/forgerock-commons/browse/self-service`
