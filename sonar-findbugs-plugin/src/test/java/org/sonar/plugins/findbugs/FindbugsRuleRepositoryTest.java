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
package org.sonar.plugins.findbugs;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.platform.ServerFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.test.i18n.RuleRepositoryTestHelper;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class FindbugsRuleRepositoryTest {
  private FindbugsRuleRepository repository;

  @Before
  public void setUpRuleRepository() {
    repository = new FindbugsRuleRepository(mock(ServerFileSystem.class), new XMLRuleParser());
  }

  @Test
  public void testLoadRepositoryFromXml() {
    List<Rule> rules = repository.createRules();

    assertThat(rules.size()).isEqualTo(418);
    for (Rule rule : rules) {
      assertThat(rule.getKey()).isNotNull();
      assertThat(rule.getConfigKey()).isNotNull();
      assertThat(rule.getName()).isNotNull();
    }
  }

  @Test
  public void should_provide_a_name_and_description_for_each_rule() {
    List<Rule> rules = RuleRepositoryTestHelper.createRulesWithNameAndDescription("findbugs", repository);

    assertThat(rules).onProperty("name").excludes(null, "");
    assertThat(rules).onProperty("description").excludes(null, "");
  }
}
