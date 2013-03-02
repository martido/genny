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
package de.martido.genny.example;

import java.util.Locale;
import java.util.ResourceBundle;

public class Quotes {

  public static void main(String[] args) {
    System.out.println(getMessage(I18N.buzz_says, Locale.ENGLISH));
    System.out.println(getMessage(I18N.buzz_says, Locale.GERMAN));
    System.out.println(getMessage(I18N.pinky_says, Locale.ENGLISH));
    System.out.println(getMessage(I18N.pinky_says, Locale.GERMAN));
    System.out.println(getMessage(I18N.brain_says, Locale.ENGLISH));
    System.out.println(getMessage(I18N.brain_says, Locale.GERMAN));
  }

  public static String getMessage(String key, Locale locale) {
    return ResourceBundle.getBundle("MessageResources", locale).getString(key);
  }

}
