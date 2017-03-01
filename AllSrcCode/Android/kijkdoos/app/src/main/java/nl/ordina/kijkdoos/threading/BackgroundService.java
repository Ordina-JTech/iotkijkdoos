package nl.ordina.kijkdoos.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;

/**
 * Created by coenhoutman on 22/02/2017.
 */

public class BackgroundService {

    @Getter
    private final ExecutorService executorService;

    public BackgroundService() {
        executorService = Executors.newSingleThreadExecutor();
    }
}
