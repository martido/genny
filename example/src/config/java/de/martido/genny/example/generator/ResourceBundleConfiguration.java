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
package de.martido.genny.example.generator;

import java.util.Arrays;
import java.util.List;

import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.provider.PropertyFileProvider;

public class ResourceBundleConfiguration implements GennyConfiguration {

  @Override
  public List<GeneratorDefinition> configure() {
    GeneratorDefinition def = new GeneratorDefinition();
    def.setTargetClass("de.martido.genny.example.generated.Resource");
    def.setBaseDirectory("src/generated/java");
    def.setFieldProvider(PropertyFileProvider.forFile("MessageResources.properties").build());
    return Arrays.asList(def);
  }

}
