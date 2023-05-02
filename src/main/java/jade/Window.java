package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;
    private static Window window = null;

    private static Scene currentScene = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown Scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init(); // initialization
        loop(); // frame loop

        // free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //set up an error callback
        GLFWErrorCallback.createPrint(System.err).set(); // error callback is initiliazed before the actual init to
        //capture errors and stuff

        if (!glfwInit()) { // init the glfw
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //CREATE THE WINDOW
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }
        //setting up the callbacks, these functions are called by glfw when the related event happens
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // make the OpenGl context current
        // this makes the window that is created to be the current context
        glfwMakeContextCurrent(glfwWindow);

        //enable V-Sync
        // This is basically asking for the back buffer to be returned every time the screen gets refreshed, or this
        // is supposed to make the frame rate of the display to be matched, 60 fps or something.
        glfwSwapInterval(1);

        // shows the window if not showing already
        glfwShowWindow(glfwWindow);

        // creates the capabilities instance to be used by all the other elements, meh
        GL.createCapabilities();
        // changes the scene to zero
        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            // whatever the events that have happened since the last frame and queue and are processed by this fun call
            glfwPollEvents();

//            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt >= 0) {
                currentScene.update(dt);
            }
            // swaps the render buffer/back buffer with the display/front buffer i.e., updates the contents
            glfwSwapBuffers(glfwWindow);

            // we are starting the time measure, we include the time that takes from end of the loop to the start of it
            // to include any time taken for calls made by OS or something
            // This is a more accurate measure of how much actual time it is taking for the loop
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
