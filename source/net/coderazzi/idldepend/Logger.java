/**
 * File: Logger.java
 * Please visit http://grasia.fdi.ucm.es/~luismi/idldepend for
 *   updates, download, license and copyright information
 **/

package net.coderazzi.idldepend;

/**
 * Interface to cover the logging (without depending on the
 *   ant's 'task', which has that functionality built in
 */
public interface Logger {
   public void log(String msg);
}
