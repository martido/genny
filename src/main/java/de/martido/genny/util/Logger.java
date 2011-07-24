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
package de.martido.genny.util;

/**
 * A very simplistic logger.
 * 
 * @author Martin Dobmeier
 */
public class Logger {

  private static final ThreadLocal<Logger> threadLocal = new ThreadLocal<Logger>();

  public static void set(Logger logger) {
    threadLocal.set(logger);
  }

  public static Logger get() {
    return threadLocal.get();
  }

  public static void remove() {
    threadLocal.remove();
  }

  private final boolean verbose;

  public Logger(boolean verbose) {
    this.verbose = verbose;
  }

  public boolean isVerbose() {
    return this.verbose;
  }

}
