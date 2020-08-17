package io.harness.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class ForEachCheck extends AbstractCheck {
  private static final String STREAM_MSG_KEY = "performance.for_each.stream";
  private static final String ENTRY_SET_MSG_KEY = "performance.for_each.entry_set";

  @Override
  public int[] getDefaultTokens() {
    return new int[] {
        TokenTypes.IDENT,
    };
  }

  @Override
  public int[] getRequiredTokens() {
    return new int[] {
        TokenTypes.IDENT,
    };
  }

  @Override
  public int[] getAcceptableTokens() {
    return getDefaultTokens();
  }

  @Override
  public void visitToken(DetailAST identifier) {
    if (!identifier.getText().equals("forEach")) {
      return;
    }

    DetailAST forEachDot = identifier.getParent();
    if (forEachDot.getType() != TokenTypes.DOT) {
      return;
    }

    DetailAST call = forEachDot.getFirstChild();
    if (call.getType() != TokenTypes.METHOD_CALL) {
      return;
    }

    DetailAST streamDot = call.getFirstChild();
    if (streamDot.getType() != TokenTypes.DOT) {
      return;
    }

    DetailAST prevFunc = streamDot.getFirstChild().getNextSibling();
    if (prevFunc.getType() != TokenTypes.IDENT) {
      return;
    }

    if (prevFunc.getText().equals("stream")) {
      log(identifier, STREAM_MSG_KEY, "SuboptimalUseOfForEach");
    } else if (prevFunc.getText().equals("entrySet")) {
      log(identifier, ENTRY_SET_MSG_KEY, "SuboptimalUseOfForEach");
    }
  }
}