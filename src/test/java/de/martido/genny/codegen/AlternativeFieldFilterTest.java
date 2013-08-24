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
import de.martido.genny.FieldFilter;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.SourceFile;
import de.martido.genny.SourceFileGenerator;

/**
 * @author Martin Dobmeier
 */
@RunWith(Parameterized.class)
public class AlternativeFieldFilterTest extends AbstractTestCase {

  public AlternativeFieldFilterTest(TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Test
  public void should_apply_alternative_field_filter() {

    GennyConfiguration conf = new GennyConfiguration() {

      @Override
      public GeneratorDefinition configure(List<String> inputFiles) {
        return new GeneratorDefinition(inputFiles) {

          @Override
          public SourceFileGenerator getSourceFileGenerator() {
            return AlternativeFieldFilterTest.this.templateEngine.getSourceFileGenerator(null);
          }

          @Override
          public FieldFilter getFieldFilter() {
            return new FieldFilter() {
              @Override
              public boolean include(Field field) {
                return field.getName().equals("property.the.ugly") ? false : true;
              }
            };
          }
        };
      }
    };

    List<String> inputFiles = Arrays.asList("src/test/resources/test.1.properties");
    SourceFile targetClass = this.createSourceFile("Alternative_Field_Filter");
    this.generate(conf, inputFiles, targetClass);
    Object obj = this.getInstanceOfGeneratedClass(targetClass);
    this.assertField(obj, "property_the_good", "property.the.good");
    this.assertField(obj, "property_the_bad", "property.the.bad");
  }

}
