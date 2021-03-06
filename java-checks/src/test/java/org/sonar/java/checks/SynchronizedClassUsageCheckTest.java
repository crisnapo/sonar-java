/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.checks;

import com.sonar.sslr.squid.checks.CheckMessagesVerifierRule;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.squid.api.SourceFile;

import java.io.File;

public class SynchronizedClassUsageCheckTest {

  @Rule
  public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

  @Test
  public void detected() {
    SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/checks/SynchronizedClassUsageCheck.java"), new SynchronizedClassUsageCheck());
    checkMessagesVerifier.verify(file.getCheckMessages())
        .next().atLine(5).withMessage("Replace the synchronized class \"Vector\" by an unsynchronized one such as \"ArrayList\" or \"LinkedList\".")
        .next().atLine(6)
        .next().atLine(7).withMessage("Replace the synchronized class \"Hashtable\" by an unsynchronized one such as \"HashMap\".")
        .next().atLine(8)
        .next().atLine(9)
        .next().atLine(12)
        .next().atLine(13).withMessage("Replace the synchronized class \"StringBuffer\" by an unsynchronized one such as \"StringBuilder\".")
        .next().atLine(16)
        .next().atLine(17)
        .next().atLine(18)
        .next().atLine(19)
        .next().atLine(22)
        .next().atLine(24)
        .next().atLine(27)
        .next().atLine(32)
        .next().atLine(34)
        .next().atLine(40)
        .next().atLine(46)
        .next().atLine(48);
  }

}
