package structs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import constants.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;

/**
 * Represents a shader program with a vertex and fragment shader.
 * A shader is a program written in GLSL that instructs OpenGL how to render a certain part of the rendering process.
 * Information about shaders obtained from https://learnopengl.com/Getting-started/Shaders
 */
public class Shader extends Handle implements Resettable {

  private String vsPath;
  private String fsPath;
  
  /**
   * Construct a shader program.
   * The vertex shader determines the coordinates at which the vertices should be drawn.
   * The fragment shader determines the color of each pixel.
   * @param vsPath Path to vertex shader file.
   * @param fsPath Path to fragment shader file.
   */
  public Shader(String vsPath, String fsPath) {
    this.vsPath = vsPath;
    this.fsPath = fsPath;
  }

  /**
   * Load this shader for use.
   */
  private void load() {
    // Attempt to load contents of the shader files.
    String vsSource = loadShaderSource(vsPath);
    if (vsSource == null) {
      Logger.error(getClass(), "Could not find source file for vertex shader: " + vsPath);
    }
    String fsSource = loadShaderSource(fsPath);
    if (fsSource == null) {
      Logger.error(getClass(), "Could not find source file for fragment shader: " + fsPath);
    }
    // Attempt to compile the vertex shader.
    int vs = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);
    // Read in shader source code.
    GL33.glShaderSource(vs, vsSource);
    GL33.glCompileShader(vs);
    // Check for error in compilation.
    if (GL33.glGetShaderi(vs, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(vs, 500));
      Logger.error(getClass(), "Could not compile vertex shader: "+ vsPath);
    }
    // Attempt to compile the fragment shader.
    int fs = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER);
    GL33.glShaderSource(fs, fsSource);
    GL33.glCompileShader(fs);
    if (GL33.glGetShaderi(fs, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(fs, 500));
      Logger.error(getClass(), "Could not compile fragment shader: " + fsPath);
    }
    // Create combined shader program.
    handle = GL33.glCreateProgram();
    // Attach vertex and fragment shader.
    GL33.glAttachShader(handle, vs);
    GL33.glAttachShader(handle, fs);
    GL33.glLinkProgram(handle);
    // Check for error in combined shader program.
    if (GL33.glGetProgrami(handle, GL33.GL_LINK_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(handle, 500));
      Logger.error(getClass(), "Could not compile shader program: " + vsPath + " | " + fsPath);
    }
    // Delete individual shaders.
    GL33.glDeleteShader(vs);
    GL33.glDeleteShader(fs);
  }
  
  /**
   * Bind this shader for use.
   */
  public void use() {
    GL33.glUseProgram(handle);
  }

  /**
   * Set the value of a uniform matrix.
   * @param name The name of the uniform attribute.
   * @param mat The value.
   */
  public void setUniformMatrix(String name, Matrix4f mat) {
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    mat.get(fb);
    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(handle, name), false, fb);
  }

  /**
   * Set the value of a uniform 4 component vector.
   * @param name The name of the uniform attribute.
   * @param vec The value.
   */
  public void setUniformVec4(String name, Vector4f vec) {
    FloatBuffer fb = BufferUtils.createFloatBuffer(4);
    vec.get(fb);
    GL33.glUniform4fv(GL33.glGetUniformLocation(handle, name), fb);
  }

  /**
   * Set the value of a uniform 3 component vector.
   * @param name The name of the uniform attribute.
   * @param vec The value.
   */
  public void setUniformVec3(String name, Vector3f vec) {
    FloatBuffer fb = BufferUtils.createFloatBuffer(3);
    vec.get(fb);
    GL33.glUniform4fv(GL33.glGetUniformLocation(handle, name), fb);
  }

  /**
   * Set the value of a uniform 2 component vector.
   * @param name The name of the uniform attribute.
   * @param vec The value.
   */
  public void setUniformVec2(String name, Vector2f vec) {
    FloatBuffer fb = BufferUtils.createFloatBuffer(2);
    vec.get(fb);
    GL33.glUniform4fv(GL33.glGetUniformLocation(handle, name), fb);
  }
  
  /**
   * Loads the contents of a single shader from a path.
   * @param path The path to the shader file.
   * @return The shader source.
   */
  private static String loadShaderSource(String path) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      reader.close();
      return sb.toString();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void reset() {
    load();
  }

}
