package nl.ordina.kijkdoos;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by coenhoutman on 15-2-2017.
 */

public class InjectableTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return Instrumentation.newApplication(MockedViewBoxApplication.class, context);
    }
}
