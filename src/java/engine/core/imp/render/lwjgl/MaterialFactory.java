package engine.core.imp.render.lwjgl;

import glextra.material.GlobalParams;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.shader.Program.ProgramLinkException;
import gltools.shader.Shader.ShaderCompileException;
import gltools.texture.Color;
import gltools.texture.TextureFactory;

import java.io.IOException;

public class MaterialFactory {
	private MaterialFactory() {

	}

	private static ResourceLocator locator;

	/**
	 * @return a white Material
	 */
	public static Material createBasicMaterial() {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			//Hack global params in there for now
			mat = MaterialXMLLoader.s_load("Materials/2d.mat", locator, GlobalParams.getInstance()).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", new Color(1, 1, 1));
		return mat;
	}

	/**
	 * @param color
	 * @return a colored Material
	 */
	public static Material createBasicMaterial(Color color) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load("Materials/2d.mat", locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", color);
		return mat;
	}

	/**
	 * @param texture
	 * @return a textured Material
	 */
	public static Material createBasicMaterial(String texture) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load("Materials/2d.mat", locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		try {
			mat.setTexture2D("materialDiffuseTexture", TextureFactory.s_loadTexture(texture, locator));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mat;
	}
}
