/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.ast;

import com.google.j2cl.ast.annotations.Visitable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for an inline (lambda) function expression.
 */
@Visitable
public class FunctionExpression extends Expression {
  // The visitors traverse the @Visitable members of the class in the order they appear.
  // The parameter declarations need to be traversed before the body.
  @Visitable final List<Variable> parameters;
  @Visitable Block body;
  private final TypeDescriptor typeDescriptor;

  private FunctionExpression(TypeDescriptor typeDescriptor, List<Variable> parameters, Block body) {
    this.parameters = parameters;
    this.body = body;
    this.typeDescriptor = typeDescriptor;
  }

  @Override
  public TypeDescriptor getTypeDescriptor() {
    return typeDescriptor;
  }

  public List<Variable> getParameters() {
    return parameters;
  }

  public Block getBody() {
    return body;
  }

  @Override
  public FunctionExpression clone() {
    List<Variable> clonedParameters = AstUtils.clone(parameters);
    Block clonedBody = AstUtils.replaceVariables(parameters, clonedParameters, body.clone());

    return FunctionExpression.newBuilder()
        .setTypeDescriptor(typeDescriptor)
        .setParameters(clonedParameters)
        .setStatements(clonedBody.getStatements())
        .build();
  }

  @Override
  public Node accept(Processor processor) {
    return Visitor_FunctionExpression.visit(processor, this);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  /** A Builder for FunctionExpression. */
  public static class Builder {
    private List<Variable> parameters = new ArrayList<>();
    private List<Statement> statements;
    private TypeDescriptor typeDescriptor;

    public static Builder from(FunctionExpression expression) {
      return new Builder()
          .setTypeDescriptor(expression.getTypeDescriptor())
          .setParameters(expression.getParameters())
          .setStatements(expression.getBody().getStatements());
    }

    public Builder setStatements(List<Statement> statements) {
      this.statements = new ArrayList<>(statements);
      return this;
    }

    public Builder setStatements(Statement... statements) {
      this.statements = Arrays.asList(statements);
      return this;
    }

    public Builder setParameters(Variable... parameters) {
      return setParameters(Arrays.asList(parameters));
    }

    public Builder setParameters(List<Variable> parameters) {
      this.parameters = parameters;
      return this;
    }

    public Builder setTypeDescriptor(TypeDescriptor typeDescriptor) {
      this.typeDescriptor = typeDescriptor;
      return this;
    }

    public FunctionExpression build() {
      return new FunctionExpression(typeDescriptor, parameters, new Block(statements));
    }
  }
}
