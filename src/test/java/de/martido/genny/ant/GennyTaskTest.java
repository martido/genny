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
package de.martido.genny.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.junit.Test;

public class GennyTaskTest {

  @Test(expected = BuildException.class)
  public void should_not_validate1() {
    GennyTask task = new GennyTask();
    task.validate();
  }

  @Test(expected = BuildException.class)
  public void should_not_validate2() {
    GennyTask task = new GennyTask();
    task.setTargetClass("com.example.TargetClass");
    task.validate();
  }

  @Test(expected = BuildException.class)
  public void should_not_validate3() {
    GennyTask task = new GennyTask();
    task.setBaseDirectory("src/main/generated");
    task.validate();
  }

  public void should_validate1() {
    GennyTask task = new GennyTask();
    task.setConfigurationClass("com.example.ConfigurationClass");
    task.validate();
  }

  public void should_validate2() {
    GennyTask task = new GennyTask();
    task.setTargetClass("com.example.TargetClass");
    task.setBaseDirectory("src/main/generated");
    task.addFileSet(new FileSet());
    task.validate();
  }

}
