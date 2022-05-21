package tk.wosaj.lambda.js;

import org.mozilla.javascript.Function;
import tk.wosaj.lambda.util.PlannedAction;

import java.util.Date;

public class JSActionPlanner {
    JSActionPlanner() {

    }

    public void plan(Date date, Function function) {
        new PlannedAction(function, date, true, new PlannedAction.JSConstant("a", 222)).start();

    }
}
