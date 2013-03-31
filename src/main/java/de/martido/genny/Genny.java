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

import de.martido.genny.util.Logger;

/**
 * The main class of Genny.
 * 
 * @author Martin Dobmeier
 */
public class Genny {

  public Genny() {
    this(false);
  }

  public Genny(boolean verbose) {
    Logger.setVerbose(verbose);
  }

  /**
   * Generate source files based on the given {@link GennyConfiguration}.
   * 
   * @param configuration
   *          The {@link GennyConfiguration}.
   * @param def
   *          The {@link GennyDefinition}.
   * @throws Exception
   *           If an error occurs.
   */
  public void generateFrom(GennyConfiguration configuration, GeneratorDefinition def)
      throws Exception {

    GeneratorDefinition newDef = configuration.configure(def);
    if (Logger.isVerbose()) {
      System.out.println(newDef.toString());
    }

    SourceFile sourceFile = newDef.getSourceFile();
    FieldProvider fieldProvider = newDef.getFieldProvider();
    FieldMapper fieldMapper = newDef.getFieldMapper();
    FieldFilter fieldFilter = newDef.getFieldFilter();
    SourceFileGenerator sourceFileGenerator = newDef.getSourceFileGenerator();
    sourceFileGenerator.generate(sourceFile, fieldProvider, fieldMapper, fieldFilter);
  }

}
