package com.takashi.mediamanager;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks how many of the application's "main" windows are currently open
 * and shuts the JVM down once the last one has been closed.
 *
 * Register every window whose closing should be able to end the whole
 * program (CountWindow, ListWindow's frames, ...). Do NOT register
 * transient/setup-only windows such as ConfigWindow's startup dialog --
 * that one is meant to close on its own without ending the program.
 *
 * Windows must actually be disposed (not just hidden) for this to see
 * them close: use setDefaultCloseOperation(DISPOSE_ON_CLOSE), since the
 * default HIDE_ON_CLOSE only hides a window without releasing it, and
 * EXIT_ON_CLOSE would bypass this tracking entirely by killing the JVM
 * as soon as a single window closes.
 */
public final class WindowManager {
    private static final AtomicInteger openWindowCount = new AtomicInteger(0);

    private WindowManager() {
    }

    public static void register(Window window) {
        openWindowCount.incrementAndGet();
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (openWindowCount.decrementAndGet() <= 0) {
                    System.exit(0);
                }
            }
        });
    }
}