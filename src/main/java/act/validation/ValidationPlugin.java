package act.validation;

/*-
 * #%L
 * ACT Framework
 * %%
 * Copyright (C) 2014 - 2017 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import act.app.App;
import act.plugin.AppServicePlugin;

import javax.inject.Singleton;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Singleton
public class ValidationPlugin extends AppServicePlugin {

    private Configuration config;
    private ValidatorFactory factory;
    public Validator validator;

    @Override
    protected void applyTo(App app) {
        init(app);
        app.registerSingleton(ValidationPlugin.class, this);
    }

    private void init(App app) {
        config = Validation.byDefaultProvider().configure();
        config.messageInterpolator(new ActValidationMessageInterpolator(config.getDefaultMessageInterpolator(), app.config()));
        ensureFactoryValidator();
    }

    private void ensureFactoryValidator() {
        if (validator != null) {
            return;
        }
        if (factory == null) {
            factory = config.buildValidatorFactory();
        }
        validator = factory.getValidator();
    }

}
