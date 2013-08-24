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

import java.util.List;

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
   * @param conf
   *          The {@link GennyConfiguration}.
   * @param inputFiles
   *          The paths to the input files from which the {@code targetClass} shall be generated.
   * @param targetClass
   *          The target class to generate.
   * @throws Exception
   *           If an error occurs.
   */
  public void generateFrom(GennyConfiguration conf, List<String> inputFiles, SourceFile targetClass)
      throws Exception {

    GeneratorDefinition def = conf.configure(inputFiles);

    if (Logger.isVerbose()) {
      System.out.println(def.toString());
    }

    FieldProvider fieldProvider = def.getFieldProvider();
    FieldMapper fieldMapper = def.getFieldMapper();
    FieldFilter fieldFilter = def.getFieldFilter();
    SourceFileGenerator sourceFileGenerator = def.getSourceFileGenerator();
    sourceFileGenerator.generate(targetClass, fieldProvider, fieldMapper, fieldFilter);
  }

}
