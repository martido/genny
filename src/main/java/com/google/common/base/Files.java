/*
 * Copyright (C) 2006 The Guava Authors
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

package com.google.common.base;

import java.io.File;
import java.io.IOException;

/**
 * All methods of this class, including Javadoc, were copied over verbatim from the Google Guava
 * libraries R9 (http://code.google.com/p/guava-libraries/).
 */
public class Files {

  /**
   * Creates any necessary but nonexistent parent directories of the specified
   * file. Note that if this operation fails it may have succeeded in creating
   * some (but not all) of the necessary parent directories.
   * 
   * @throws IOException if an I/O error occurs, or if any necessary but
   *           nonexistent parent directories of the specified file could not be
   *           created.
   * @since 4
   */
  public static void createParentDirs(File file) throws IOException {
    File parent = file.getCanonicalFile().getParentFile();
    if (parent == null) {
      /*
       * The given directory is a filesystem root. All zero of its ancestors
       * exist. This doesn't mean that the root itself exists -- consider x:\ on
       * a Windows machine without such a drive -- or even that the caller can
       * create it, but this method makes no such guarantees even for non-root
       * files.
       */
      return;
    }
    parent.mkdirs();
    if (!parent.isDirectory()) {
      throw new IOException("Unable to create parent directories of " + file);
    }
  }

  /**
   * Creates an empty file or updates the last updated timestamp on the
   * same as the unix command of the same name.
   * 
   * @param file the file to create or update
   * @throws IOException if an I/O error occurs
   */
  public static void touch(File file) throws IOException {
    if (!file.createNewFile()
        && !file.setLastModified(System.currentTimeMillis())) {
      throw new IOException("Unable to update modification time of " + file);
    }
  }

}
