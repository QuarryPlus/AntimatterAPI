package muramasa.itech.client.render.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

public class ModelBase implements IModel {

    public static final TRSRTransformation NORTH = TRSRTransformation.from(EnumFacing.SOUTH);
    public static final TRSRTransformation SOUTH = TRSRTransformation.from(EnumFacing.NORTH);
    public static final TRSRTransformation EAST = TRSRTransformation.from(EnumFacing.WEST);
    public static final TRSRTransformation WEST = TRSRTransformation.from(EnumFacing.EAST);
    public static final TRSRTransformation DOWN = TRSRTransformation.from(EnumFacing.UP);
    public static final TRSRTransformation UP = TRSRTransformation.from(EnumFacing.DOWN);

    public static IBakedModel missingBaked;

    private static HashMap<String, Collection<ResourceLocation>> textureLookup = new HashMap<>();

    private String name;
    private IModelState state;
    private VertexFormat format;
    private Function<ResourceLocation, TextureAtlasSprite> textureGetter;

    public ModelBase(String name) {
        this.name = name;
        textureLookup.put(name, new ArrayList<>());
    }

    public IBakedModel bakeModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
        return ModelLoaderRegistry.getMissingModel().bake(state, format, getter);
    }

    @Override
    public IBakedModel bake(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        try {
            state = modelState;
            format = vertexFormat;
            textureGetter = bakedTextureGetter;
            if (missingBaked == null) {
                missingBaked = ModelLoaderRegistry.getMissingModel().bake(modelState, vertexFormat, bakedTextureGetter);
            }
            return bakeModel(modelState, vertexFormat, bakedTextureGetter);
        } catch (Exception e) {
            System.err.println(name + ".bake() failed due to exception:" + e);
            e.printStackTrace();
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return textureLookup.get(name);
    }

    public void addTexture(ResourceLocation texture) {
        textureLookup.get(name).add(texture);
    }

    public void addTextures(ResourceLocation[] textures) {
        addTextures(Arrays.asList(textures));
    }

    public void addTextures(Collection<ResourceLocation> textures) {
        textureLookup.get(name).addAll(textures);
    }

    public IBakedModel texAndBake(IModel model, String[] elements, IModelState state, ResourceLocation[] textures) {
        return tex(model, elements, textures).bake(state, format, textureGetter);
    }

    public IBakedModel texAndBake(IModel model, String[] elements, ResourceLocation[] textures) {
        return tex(model, elements, textures).bake(state, format, textureGetter);
    }

    public IBakedModel texAndBake(IModel model, String element, IModelState state, ResourceLocation texture) {
        return tex(model, element, texture).bake(state, format, textureGetter);
    }

    public IBakedModel texAndBake(IModel model, String element, ResourceLocation texture) {
        return tex(model, element, texture).bake(state, format, textureGetter);
    }

//    public static IBakedModel[] getBaked(String prefix, String modelName) {
//        return bakedModelLookup.get(prefix + modelName);
//    }
//
//    public static void addBaked(String prefix, String modelName, IBakedModel[] models) {
//        bakedModelLookup.put(prefix + modelName, models);
//    }
//
//    public static void addBaked(String prefix, String modelName, IBakedModel model) {
//        bakedModelLookup.put(prefix + modelName, new IBakedModel[]{model});
//    }

    public static IModel load(ModelResourceLocation loc) {
        try {
            return ModelLoaderRegistry.getModel(loc);
        } catch (Exception e) {
            System.err.println("ModelBase.load() failed due to " + e + ":");
            e.printStackTrace();
            return ModelLoaderRegistry.getMissingModel();
        }
    }

    public static IModel tex(IModel model, String[] elements, ResourceLocation[] textures) {
        for (int i = 0; i < elements.length; i++) {
            model = tex(model, elements[i], textures[i]);
        }
        return model;
    }

    public static IModel tex(IModel model, String element, ResourceLocation texture) {
        try {
            return model.retexture(ImmutableMap.of(element, texture.toString()));
        } catch (Exception e) {
            System.err.println("ModelBase.tex() failed due to " + e + ":");
            e.printStackTrace();
            return model;
        }
    }
}
