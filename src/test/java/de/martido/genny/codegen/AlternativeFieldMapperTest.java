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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.martido.genny.Field;
import de.martido.genny.FieldMapper;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.SourceFile;
import de.martido.genny.SourceFileGenerator;

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

    GennyConfiguration conf = new GennyConfiguration() {

      @Override
      public GeneratorDefinition configure(List<String> inputFiles) {
        return new GeneratorDefinition(inputFiles) {

          @Override
          public SourceFileGenerator getSourceFileGenerator() {
            return AlternativeFieldMapperTest.this.templateEngine.getSourceFileGenerator(null);
          }

          @Override
          public FieldMapper getFieldMapper() {
            return new FieldMapper() {
              @Override
              public Field map(Field f) {
                String name = this.transform(f.getName()).toUpperCase();
                String value = this.transform((String) f.getValue());
                return new Field(name, value, f.getJavadoc());
              }

              private String transform(String s) {
                return s.replaceFirst("property\\.", "").replaceAll("\\.", "_");
              }
            };
          }
        };
      }
    };

    List<String> inputFiles = Arrays.asList("src/test/resources/test.1.properties");
    SourceFile targetClass = this.createSourceFile("Alternative_Field_Mapper");
    this.generate(conf, inputFiles, targetClass);
    Object obj = this.getInstanceOfGeneratedClass(targetClass);
    this.assertField(obj, "THE_GOOD", "the_good");
    this.assertField(obj, "THE_BAD", "the_bad");
    this.assertField(obj, "THE_UGLY", "the_ugly");
  }

}
