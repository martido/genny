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

import de.martido.genny.SourceFileGenerator;

/**
 * @author Martin Dobmeier
 */
public enum TemplateEngine {

  STRINGTEMPLATE("stg") {

    @Override
    public SourceFileGenerator getSourceFileGenerator(String template) {
      if (template != null) {
        String fileName = template + "." + this.getExtension();
        return new StringTemplateSourceFileGenerator(fileName);
      }
      return new StringTemplateSourceFileGenerator();
    }

  },

  VELOCITY("vtl") {

    @Override
    public SourceFileGenerator getSourceFileGenerator(String template) {
      if (template != null) {
        String fileName = template + "." + this.getExtension();
        return new VelocitySourceFileGenerator(fileName);
      }
      return new VelocitySourceFileGenerator();
    }

  };

  private final String extension;

  private TemplateEngine(String extension) {
    this.extension = extension;
  }

  public String getExtension() {
    return this.extension;
  }

  public abstract SourceFileGenerator getSourceFileGenerator(String template);

}
