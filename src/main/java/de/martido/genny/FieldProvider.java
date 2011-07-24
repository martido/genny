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
 * A provider for {@link Field}s.
 * 
 * @author Martin Dobmeier
 */
public interface FieldProvider {

  /**
   * Provides a list of {@link Field}s.
   * 
   * @param fieldFilter
   *          A {@link FieldFilter} to control.
   * @return A list of {@link Field}s.
   * @throws Exception If an error occurs.
   */
  List<Field> provide(FieldFilter fieldFilter) throws Exception;

}
