package jade;


import com.sun.net.httpserver.Authenticator;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3  aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSrc="#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //positions             //color
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f,0.0f, 1.0f // Bottom right
           -0.5f, 0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top Left
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f // Top right
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f // Bottom right
    };

    private int[] elementArray = {
            /*
                x       x


                x       x
             */
            2, 1, 0, //Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        // compiling and linking the shaders


        // loading and compiling the shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // PASS THE SHADER SOURCE CODE TO THE GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);


        // Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_COMPILE_STATUS);
            System.out.println("Error: 'defaultShader.glsl'\n\t Vertex Shader Compilation Failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // loading and compiling the shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // PASS THE SHADER SOURCE CODE TO THE GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);


        // Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
            System.out.println("Error: 'defaultShader.glsl'\n\t Fragment Shader Compilation Failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and checkfor errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl'\n\t");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false: "";
        }

        // generate vao, vbo, ebo and sending them to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer

    }


    @Override
    public void update(float dt) {

    }
}
