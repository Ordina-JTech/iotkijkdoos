package nl.ordina.kijkdoos;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.internal.util.AndroidRunnerParams;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.junit.runners.model.InitializationError;

/**
 * Created by coenhoutman on 15-2-2017.
 */

public class InjectableTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return Instrumentation.newApplication(MockedViewBoxApplication.class, context);
    }
}
