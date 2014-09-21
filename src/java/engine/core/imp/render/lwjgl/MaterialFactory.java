package engine.core.imp.render.lwjgl;

import glextra.material.GlobalParams;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.shader.Program.ProgramLinkException;
import gltools.shader.Shader.ShaderCompileException;
import gltools.texture.Color;

import java.io.IOException;

public class MaterialFactory {
	private MaterialFactory() {

	}

	private static ResourceLocator locator;

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

		mat.setColor("materialDiffuseColor", new Color(1, 0, 1));
		return mat;
	}
}
