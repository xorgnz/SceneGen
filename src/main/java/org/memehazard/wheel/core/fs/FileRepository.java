package org.memehazard.wheel.core.fs;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class FileRepository
{
    private File d_root;


    public String getPath()
    {
        return d_root.getAbsolutePath();
    }


    public boolean isValidAssetFile(String filename)
    {
        String ext = FilenameUtils.getExtension(filename);
        
        return "x3d".equalsIgnoreCase(ext) || "obj".equalsIgnoreCase(ext);
    }


    public void removeFile(String filename)
    {
        File f_out = new File(d_root, filename);
        f_out.delete();
    }


    public File retrieveFile(String filename)
    {
        File f_out = new File(d_root, filename);
        return f_out;
    }


    public void setPath(String path)
    {
        d_root = new File(path);
    }


    public File toUniqueFile(String s)
    {
        String base = FilenameUtils.getBaseName(s);
        String ext = FilenameUtils.getExtension(s);
        String name = base + "." + ext;

        File f_unique = new File(d_root, name);

        if (!f_unique.exists())
            return f_unique;
        else
        {
            return toUniqueFile(base + "_" + System.nanoTime() + "." + ext);
        }
    }
}
