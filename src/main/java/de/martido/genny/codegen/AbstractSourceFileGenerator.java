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

import java.io.File;
import java.io.IOException;

import com.google.common.base.Files;

import de.martido.genny.SourceFileGenerator;

/**
 * An abstract base class for {@link SourceFileGenerator}s.
 * 
 * @author Martin Dobmeier
 */
public abstract class AbstractSourceFileGenerator implements SourceFileGenerator {

  /**
   * Creates the actual source file and, if necessary, all parent directories.
   * 
   * @param targetDefinition
   *          <i>mandatory</i> - the {@link TargetDefinition}.
   * @return A {@code File}.
   * @throws IOException
   *           If an I/O error occured.
   */
  protected File createFile(TargetDefinition targetDefinition) throws IOException {
    File file = new File(targetDefinition.asPath());
    Files.createParentDirs(file);
    Files.touch(file);
    return file;
  }

}
