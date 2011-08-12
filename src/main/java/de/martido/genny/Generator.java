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
package de.martido.genny;

import de.martido.genny.codegen.SimpleSourceFileGenerator;
import de.martido.genny.util.Logger;

/**
 * The main class of Genny.
 * 
 * @author Martin Dobmeier
 */
public class Generator {

  public Generator() {
    this(false);
  }

  public Generator(boolean verbose) {
    Logger.set(new Logger(verbose));
  }

  /**
   * Generate source files based on the given {@link GennyConfiguration}.
   * 
   * @param configuration
   *          The {@link GennyConfiguration}.
   * @throws Exception
   *           If an error occurs.
   */
  public void generateFrom(GennyConfiguration configuration) throws Exception {

    try {
      for (GeneratorDefinition def : configuration.configure()) {
        if (Logger.get().isVerbose()) {
          System.out.println(def.toString());
        }
        this.generate(def);
      }
    } finally {
      Logger.remove();
    }
  }

  private void generate(GeneratorDefinition def) throws Exception {

    SourceFile sourceFile = this.createSourceFile(def);
    FieldProvider fieldProvider = def.getFieldProvider();
    FieldMapper fieldMapper = this.createFieldMapper(def);
    FieldFilter fieldFilter = this.createFieldFilter(def);
    SourceFileGenerator sourceFileGenerator = this.createSourceFileGenerator(def);
    sourceFileGenerator.generate(sourceFile, fieldProvider, fieldMapper, fieldFilter);
  }

  private SourceFile createSourceFile(GeneratorDefinition def) {
    return new SourceFile(def.getTargetClass(), def.getBaseDirectory());
  }

  private FieldMapper createFieldMapper(GeneratorDefinition def) {
    return def.getFieldMapper() == null ? FieldMapper.DEFAULT : def.getFieldMapper();
  }

  private FieldFilter createFieldFilter(GeneratorDefinition def) {
    return def.getFieldFilter() == null ? FieldFilter.INCLUDE_ALL : def.getFieldFilter();
  }

  private SourceFileGenerator createSourceFileGenerator(GeneratorDefinition def) {
    return def.getSourceFileGenerator() == null
        ? new SimpleSourceFileGenerator()
        : def.getSourceFileGenerator();
  }

}
