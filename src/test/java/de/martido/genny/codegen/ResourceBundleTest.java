/*
 * Copyright 2011 Martin Dobmeier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.martido.genny.codegen;

import static de.martido.genny.provider.PropertyFileProvider.forFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.martido.genny.GeneratorDefinition;

/**
 * @author Martin Dobmeier
 */
@RunWith(Parameterized.class)
public class ResourceBundleTest extends AbstractTestCase {

  public ResourceBundleTest(TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Test
  public void should_generate_from_resource_bundle() {

    GeneratorDefinition def = new GeneratorDefinition();
    def.setTargetClass(this.templateEngine.getExtension() + ".Resource_Bundle");
    def.setBaseDirectory(BASE_DIRECTORY);
    def.setFieldProvider(forFile("src/test/resources/MessageResources.properties").build());
    this.generate(def);

    Object obj = this.getInstanceOfGeneratedClass(def);
    this.assertField(obj, "buzz_says", "buzz.says");
    this.assertField(obj, "yoda_says", "yoda.says");
  }

}
