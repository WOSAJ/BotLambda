package tk.wosaj.lambda.util;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

import java.util.*;

public class PlannedAction implements Cloneable {
    private final Function function;
    private final Date date;
    private final List<JSConstant> constants = new ArrayList<>();
    protected final boolean hasRecursion;

    public PlannedAction(Function function, Date date, boolean hasRecursion, JSConstant... constants) {
        this.function = function;
        this.date = date;
        this.hasRecursion = hasRecursion;
        this.constants.addAll(Arrays.asList(constants));
    }

    public Function getFunction() {
        return function;
    }

    public Date getDate() {
        return date;
    }

    public void run() {
        Context enter = Context.enter();
        ScriptableObject scriptableObject = enter.initStandardObjects();
        constants.forEach(c -> ScriptableObject.putConstProperty(scriptableObject, c.name, c.object));
        ScriptableObject.putConstProperty(scriptableObject, "recursion", hasRecursion);
        function.call(enter, scriptableObject, scriptableObject, new Object[]{});
        enter.close();
    }

    public void start() {
        PlannedAction tPlannedAction = this;
        //TODO Thread pool execution
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                tPlannedAction.run();
            }
        }, date);
    }

    @Override
    public PlannedAction clone() {
        try {
            return (PlannedAction) super.clone();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    public static final class JSConstant {
        private final String name;
        private final Object object;

        public JSConstant(String name, Object object) {
            this.name = name;
            this.object = object;
        }

        public String getName() {
            return name;
        }

        public Object getObject() {
            return object;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JSConstant that = (JSConstant) o;
            return name.equals(that.name) && object.equals(that.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, object);
        }

        @Override
        public String toString() {
            return "JSConstant{" +
                    "name='" + name + '\'' +
                    ", object=" + object +
                    '}';
        }
    }
}
