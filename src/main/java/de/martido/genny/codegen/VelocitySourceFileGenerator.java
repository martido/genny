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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.FieldProvider;
import de.martido.genny.SourceFileGenerator;

/**
 * An implementation of {@link SourceFileGenerator} that uses the Apache Velocity library to
 * generate source files from a template.
 * <p>
 * By default the generator uses the template {@code velocityDefault.vtl}. However, callers can
 * provide their own template in the constructor. If doing so, keep in mind that the Velocity engine
 * is configured to use the {@code ClasspathResourceLoader} resource loader in addition to the
 * default {@code FileResourceLoader}. This implementation assumes the presence of the following
 * variables:
 * <ul>
 * <li>$packageName: The name of the package that the generated Java class resides in.</li>
 * <li>$className: The name of the generated Java class.</li>
 * <li>$fields: The fields of the generated Java class.</li>
 * <li>$fieldMapper: The {@link FieldMapper}</li>
 * </ul>
 * 
 * @author Martin Dobmeier
 */
public class VelocitySourceFileGenerator extends AbstractSourceFileGenerator {

  /**
   * The name of a template group file.
   */
  private final String templateFileName;

  public VelocitySourceFileGenerator() {
    this.templateFileName = "velocityDefault.vtl";
  }

  public VelocitySourceFileGenerator(String templateFileName) {
    this.templateFileName = templateFileName;
  }

  @Override
  public void generate(TargetDefinition targetDefinition,
      FieldProvider fieldProvider,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception {

    Writer writer = null;
    try {

      VelocityEngine engine = new VelocityEngine();
      engine.init(this.loadProperties());

      VelocityContext context = new VelocityContext();
      context.put("packageName", targetDefinition.getPackageName());
      context.put("className", targetDefinition.getSimpleName());
      context.put("fields", fieldProvider.provide(fieldFilter));
      context.put("fieldMapper", fieldMapper);

      File file = this.createFile(targetDefinition);
      writer = new BufferedWriter(new FileWriter(file));

      Template template = engine.getTemplate(this.templateFileName, "UTF-8");
      template.merge(context, writer);

      writer.flush();

    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  private Properties loadProperties() throws IOException {
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("velocity.properties");
    Properties properties = new Properties();
    properties.load(in);
    return properties;
  }

  @Override
  public String toString() {
    return "VelocitySourceFileGenerator [templateFileName=" + this.templateFileName + "]";
  }

}
