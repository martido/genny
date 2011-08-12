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
 * A strategy for applying a mapping transformation to a {@link Field}.
 * <p>
 * For example, if some source provides a field name that contains characters that are not allowed
 * as per the Java Language Specification, a {@code FieldMapper} may be specified that transforms
 * the field name to an appropriate name.
 * 
 * @author Martin Dobmeier
 */
public interface FieldMapper {

  /**
   * Applies a mapping transformation to the given {@link Field}.
   * 
   * @param field
   *          <i>mandatory<i> - the {@link Field} to apply the mapping to.
   * @return A new {@link Field}.
   */
  Field map(Field field);

  /**
   * Default implementation that simply replaces each dot (if present) in the name of a field with
   * an underscore. E.g. a field name of the form "property.something" becomes a field with name
   * "property_something". The field value is left unchanged.
   */
  public static final FieldMapper DEFAULT = new FieldMapper() {

    public Field map(Field f) {
      String name = f.getName().replaceAll("\\.", "_");
      return new Field(name, f.getValue(), f.getJavadoc());
    }

    @Override
    public String toString() {
      return "DefaultFieldMapper";
    }
  };

  /**
   * An implementation that doesn't apply any mapping.
   */
  public static final FieldMapper NO_MAPPING = new FieldMapper() {

    public Field map(Field f) {
      return f;
    }

    @Override
    public String toString() {
      return "NoFieldMapper";
    }
  };

}
