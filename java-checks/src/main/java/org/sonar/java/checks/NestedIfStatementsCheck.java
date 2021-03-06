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

import org.sonar.api.rule.RuleKey;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.DoWhileStatementTree;
import org.sonar.plugins.java.api.tree.ForEachStatement;
import org.sonar.plugins.java.api.tree.ForStatementTree;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.SwitchStatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.WhileStatementTree;

@Rule(
  key = NestedIfStatementsCheck.KEY,
  priority = Priority.MINOR)
@BelongsToProfile(title = "Sonar way", priority = Priority.MINOR)
public class NestedIfStatementsCheck extends BaseTreeVisitor implements JavaFileScanner {

  public static final String KEY = "S134";
  private static final RuleKey RULE_KEY = RuleKey.of(CheckList.REPOSITORY_KEY, KEY);

  private static final int DEFAULT_MAX = 3;

  @RuleProperty(defaultValue = "" + DEFAULT_MAX)
  public int max = DEFAULT_MAX;

  private JavaFileScannerContext context;
  private int nestingLevel;

  @Override
  public void scanFile(final JavaFileScannerContext context) {
    this.context = context;
    this.nestingLevel = 0;
    scan(context.getTree());
  }

  @Override
  public void visitIfStatement(IfStatementTree tree) {
    nestingLevel++;
    checkNesting(tree);
    visit(tree);
    nestingLevel--;
  }

  @Override
  public void visitForStatement(ForStatementTree tree) {
    nestingLevel++;
    checkNesting(tree);
    super.visitForStatement(tree);
    nestingLevel--;
  }

  @Override
  public void visitForEachStatement(ForEachStatement tree) {
    nestingLevel++;
    checkNesting(tree);
    super.visitForEachStatement(tree);
    nestingLevel--;
  }

  @Override
  public void visitWhileStatement(WhileStatementTree tree) {
    nestingLevel++;
    checkNesting(tree);
    super.visitWhileStatement(tree);
    nestingLevel--;
  }

  @Override
  public void visitDoWhileStatement(DoWhileStatementTree tree) {
    nestingLevel++;
    checkNesting(tree);
    super.visitDoWhileStatement(tree);
    nestingLevel--;
  }

  @Override
  public void visitSwitchStatement(SwitchStatementTree tree) {
    nestingLevel++;
    checkNesting(tree);
    super.visitSwitchStatement(tree);
    nestingLevel--;
  }

  private void checkNesting(Tree tree) {
    if (nestingLevel == max + 1) {
      context.addIssue(tree, RULE_KEY, "Refactor this code to not nest more than " + max + " if/for/while/switch statements.");
    }
  }

  private void visit(IfStatementTree tree) {
    scan(tree.condition());
    scan(tree.thenStatement());

    StatementTree elseStatementTree = tree.elseStatement();
    if (elseStatementTree != null && elseStatementTree.is(Tree.Kind.IF_STATEMENT)) {
      visit((IfStatementTree) elseStatementTree);
    } else {
      scan(elseStatementTree);
    }
  }

}
