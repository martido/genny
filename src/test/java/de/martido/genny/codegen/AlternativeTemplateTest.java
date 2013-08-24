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

import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.SourceFile;
import de.martido.genny.SourceFileGenerator;

/**
 * @author Martin Dobmeier
 */
@RunWith(Parameterized.class)
public class AlternativeTemplateTest extends AbstractTestCase {

  public AlternativeTemplateTest(TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Test
  public void should_apply_alternative_template() {

    GennyConfiguration conf = new GennyConfiguration() {

      @Override
      public GeneratorDefinition configure(List<String> inputFiles) {
        return new GeneratorDefinition(inputFiles) {

          @Override
          public SourceFileGenerator getSourceFileGenerator() {
            return AlternativeTemplateTest.this.templateEngine
                .getSourceFileGenerator("alternativeTemplate");
          }
        };
      }
    };

    List<String> inputFiles = Arrays.asList("src/test/resources/test.1.properties");
    SourceFile targetClass = this.createSourceFile("Alternative_Template");
    this.generate(conf, inputFiles, targetClass);
  }

}
