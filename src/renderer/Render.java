package renderer;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

import java.util.MissingResourceException;

public class Render {
    private ImageWriter imageWriter;
    private Camera camera;
    public RayTracerBase rayTracerBase;


    public RayTracerBase getRayTracerBase() {
        return rayTracerBase;
    }

    public Camera getCamera() {
        return camera;
    }

    public ImageWriter getImageWriter() {
        return imageWriter;
    }

    public Render setRayTracerBase(RayTracerBase rayTracerBase) {
        this.rayTracerBase = rayTracerBase;
        return this;
    }


    public Render setCamera(Camera camera) {
        this.camera = camera;
        return this;
    }

    public Render setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    public void renderImage() {
        if (imageWriter == null)
            throw new MissingResourceException("imageWriter is null", "Render", "imageWriter");
        if (camera == null)
            throw new MissingResourceException("camera is null", "Render", "Camera");
        if (rayTracerBase == null)
            throw new MissingResourceException("rayTracerBase is null", "Render", "rayTracerBase");



        int x = imageWriter.getNx();
        int y = imageWriter.getNy();


        for (int i=0;i<x;i++)
        {
            for (int j =0;j<y;++j)
            {
                Ray pixelRay = camera.constructRayThroughPixel(x,y,i,j);
                Color pixelColor = rayTracerBase.traceRay(pixelRay);
                imageWriter.writePixel(i,j,pixelColor);
            }
        }

    }
    public void renderImageSuperSampling() {
        if (imageWriter == null)
            throw new MissingResourceException("imageWriter is null", "Render", "imageWriter");
        if (camera == null)
            throw new MissingResourceException("camera is null", "Render", "Camera");
        if (rayTracerBase == null)
            throw new MissingResourceException("rayTracerBase is null", "Render", "rayTracerBase");



        int x = imageWriter.getNx();
        int y = imageWriter.getNy();


        for (int i=0;i<x;i++)
        {
            for (int j =0;j<y;++j)
            {
                Color pixelColor = Color.BLACK;
                for (int k =-1;k<2;++k)
                {
                    for (int f =-1;f<2;++f)
                    {
                        Ray pixelRay = camera.constructRayThroughPixelSuperSampling(x,y,i,j,k,f);
                        pixelColor= pixelColor.add(rayTracerBase.traceRay(pixelRay));
                    }
                }
                pixelColor =pixelColor.reduce(9);
                imageWriter.writePixel(i,j,pixelColor);
            }
        }
    }


    public void printGrid(int interval, Color color)
    {

        if (imageWriter == null)
            throw new MissingResourceException("imageWriter is null", "Render", "imageWriter");
        int x = imageWriter.getNx();
        int y = imageWriter.getNy();


        for (int i=0;i<x;i++)
        {
            for (int j =0;j<y;++j)
            {
             if(i%interval ==0||j%interval ==0)
                 imageWriter.writePixel(i,j,color);
            }
        }
    }
    public void writeToImage()
    {
        if (imageWriter == null)
            throw new MissingResourceException("imageWriter is null", "Render", "imageWriter");
        imageWriter.writeToImage();
    }



}
