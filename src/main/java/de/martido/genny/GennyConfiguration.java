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

/**
 * Configuration for the generation of source files.
 * 
 * @author Martin Dobmeier
 */
public interface GennyConfiguration {

  /**
   * @param inputFiles
   *          The paths to the input files from which the target class shall be generated.
   * @return A {@link GeneratorDefinition}.
   */
  GeneratorDefinition configure(List<String> inputFiles);

}
