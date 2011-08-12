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
 * A filter for selecting fields that should be included in a generated source file.
 * 
 * @author Martin Dobmeier
 */
public interface FieldFilter {

  /**
   * Applies the filter to the given {@link Field}.
   * 
   * @param field
   *          <i>mandatory<i> - the {@link Field} to apply the filter to.
   * @return {@code True}, if {@code field} should be included; {@code false} otherwise.
   */
  boolean include(Field field);

  /**
   * Default implementation that doesn't apply any filter.
   */
  public static final FieldFilter INCLUDE_ALL = new FieldFilter() {

    @Override
    public boolean include(Field field) {
      return true;
    }

    @Override
    public String toString() {
      return "DefaultFieldFilter";
    }
  };

}
