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

import static de.martido.genny.provider.PropertyFileProvider.forFiles;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.martido.genny.GeneratorDefinition;

/**
 * @author Martin Dobmeier
 */
@RunWith(Parameterized.class)
public class MultiplePropertyFilesTest extends AbstractTestCase {

  public MultiplePropertyFilesTest(TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Test
  public void should_generate_from_multiple_property_files() {

    GeneratorDefinition def = new GeneratorDefinition(
        this.templateEngine.getExtension() + ".Multiple_Property_Files",
        BASE_DIRECTORY,
        Arrays.asList(
            "src/test/resources/test.1.properties",
            "src/test/resources/test.2.properties"));
    def.setFieldProvider(forFiles(def.getInputFiles()).build());
    this.generate(def);

    Object obj = this.getInstanceOfGeneratedClass(def);
    this.assertField(obj, "property_the_good", "property.the.good");
    this.assertField(obj, "property_the_bad", "property.the.bad");
    this.assertField(obj, "property_the_ugly", "property.the.ugly");
    this.assertField(obj, "property_hero", "property.hero");
    this.assertField(obj, "property_villain", "property.villain");
  }
}
