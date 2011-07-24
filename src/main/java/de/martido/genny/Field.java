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

/**
 * A single field that is included in the generated source file. A field is comprised of:
 * <ul>
 * <li>a name,</li>
 * <li>a value, and</li>
 * <li>a Javadoc comment.</li>
 * </ul>
 * The value of a field can be anything. Template engines usually provide a kind of expression
 * language that is able to access an object's properties reflectively.
 * 
 * @author Martin Dobmeier
 */
public class Field {

  /**
   * The name of the field.
   */
  private final String name;

  /**
   * The value of the field.
   */
  private final Object value;

  /**
   * The field's JavaDoc comment.
   */
  private final String javadoc;

  public Field(String name, Object value) {
    this.name = name;
    this.value = value;
    this.javadoc = null;
  }

  public Field(String name, Object value, String javadoc) {
    this.name = name;
    this.value = value;
    this.javadoc = javadoc;
  }

  public String getName() {
    return this.name;
  }

  public Object getValue() {
    return this.value;
  }

  public String getJavadoc() {
    return this.javadoc;
  }

  @Override
  public String toString() {
    return "Field [name=" + this.name + ", value=" + this.value + ", javadoc=" + this.javadoc + "]";
  }

}
