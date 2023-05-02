package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance; // singleton design pattern, there is only one object of this class ever.
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3]; // assuming that there are only three buttons on the mouse
    private boolean isDragging;

    // the constructor is private because we don't want any other code to initialize this.
    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    // this gets us the instance object if it exists, if not it will call the constructor and then return
    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    // Callback
    /* The GLFW when mousepos changes will call this function, the function signature is defined by the
    GLFW docs, we register this function with GLFW, for it to be mouse position call back, and we take the information
    passed by the GLFW and use it as we need.
    isDragging is set to true if any of the buttons is pressed, we don't know if two button are dragging with this
    implementation.
     */
    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos; // the reason we are get(), is because we want the methods to be static, which makes
        get().lastY = get().yPos; // it easy when setting up the callbacks
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    /*
    Checks if the action passed is press or release, and updates the mouseButtonPressed array as required based on the
    button number passed, and also sets isDragging to false for release action.
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    /*
    Gets the Offset and updates the scrollX and scrollY as required.
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }
    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button< get().mouseButtonPressed.length) { // checking if the button passed is one of three mouse buttons
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
