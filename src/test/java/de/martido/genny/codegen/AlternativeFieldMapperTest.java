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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.martido.genny.Field;
import de.martido.genny.FieldMapper;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.provider.PropertyFileProvider;

/**
 * @author Martin Dobmeier
 */
@RunWith(Parameterized.class)
public class AlternativeFieldMapperTest extends AbstractTestCase {

  public AlternativeFieldMapperTest(TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Test
  public void should_apply_alternative_field_mapper() {

    GeneratorDefinition def = new GeneratorDefinition();
    def.setTargetClass(this.templateEngine.getExtension() + ".Alternative_Field_Mapper");
    def.setBaseDirectory(BASE_DIRECTORY);
    def.setFieldProvider(PropertyFileProvider.forFile("test.1.properties").build());

    // A prefix should be excluded from both field names and value.
    // Also, field names should be upper case.
    def.setFieldMapper(new FieldMapper() {
      public Field map(Field f) {
        String name = this.transform(f.getName()).toUpperCase();
        String value = this.transform((String) f.getValue());
        return new Field(name, value, f.getJavadoc());
      }

      private String transform(String s) {
        return s.replaceFirst("property\\.", "").replaceAll("\\.", "_");
      }
    });

    this.generate(def);

    Object obj = this.getInstanceOfGeneratedClass(def);
    this.assertField(obj, "THE_GOOD", "the_good");
    this.assertField(obj, "THE_BAD", "the_bad");
    this.assertField(obj, "THE_UGLY", "the_ugly");
  }

}
